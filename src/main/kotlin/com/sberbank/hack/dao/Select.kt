package com.sberbank.hack.dao

import com.sberbank.hack.dao.models.LogFile
import com.sberbank.hack.dao.models.Operation
import org.springframework.stereotype.Component
import java.sql.Connection
import java.sql.Date
import java.sql.PreparedStatement
import java.sql.ResultSet
import kotlin.collections.ArrayList

@Component
class Select {
    fun logFiles(connect: Connection,
                 logDate : Date): LogFile {
        val sql: String = "select\n" +
                "a.recid\n" +
                ",a.stamp\n" +
                ",a.name\n" +
                ",a.first_change#\n" +
                ",a.next_change#\n" +
                ",a.first_time\n" +
                ",a.next_time" +
                "from v${'$'}archived_log a \n" +
                "where trunc(a.first_time) = to_date(?)\n" +
                "and rownum < 2\n" +
                "order by a.first_time desc"
        val preparedStatement: PreparedStatement = connect.prepareStatement(sql)
        preparedStatement.setDate(1, logDate)

        val resultSet: ResultSet = preparedStatement.executeQuery()

        return LogFile(
                resultSet.getLong("RECID"),
                resultSet.getLong("STAMP"),
                resultSet.getString("NAME"),
                resultSet.getLong("FIRST_CHANGE#"),
                resultSet.getLong("NEXT_CHANGE#"),
                resultSet.getLong("FIRST_TIME"),
                resultSet.getLong("NEXT_TIME")
        )

    }

    fun operations(connect: Connection,
                   scn: Long,
                   rownum: Long): Collection<Operation> {

        val sql: String = "select \n" +
                "t.scn\n" +
                ",t.start_scn\n" +
                ",t.start_timestamp\n" +
                ",t.xid\n" +
                ",t.sql_redo\n" +
                ",t.sql_undo\n" +
                ",t.info\n" +
                ",t.redo_value\n" +
                ",t.xid\n" +
                " from data_lgmr t\n" +
                //TODO V$LOGMNR_CONTENTS
                " where rownum <= ?\n" +
                "and t.scn > ?\n"+
                "order by t.scn desc"
        val preparedStatement: PreparedStatement = connect.prepareStatement(sql)
        preparedStatement.setLong(1, scn)
        preparedStatement.setLong(2, rownum)

        val resultSet: ResultSet = preparedStatement.executeQuery()

        val operationList = ArrayList<Operation>()

        while (resultSet.next()) {
            operationList.add(Operation(
                    resultSet.getLong("SCN"),
                    resultSet.getLong("START_SCN"),
                    resultSet.getString("START_TIMESTAMP"),
                    resultSet.getString("XID"),
                    resultSet.getString("SQL_REDO"),
                    resultSet.getString("SQL_UNDO"),
                    resultSet.getString("REDO_VALUE"),
                    resultSet.getString("XID")
            ))
        }
        return operationList
    }
}