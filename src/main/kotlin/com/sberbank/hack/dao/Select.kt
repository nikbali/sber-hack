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
        val sql: String = "select \n" +
                "a.recid \n" +
                ", a.stamp \n" +
                ", a.name \n" +
                ", a.first_change# \n" +
                ", a.next_change# \n" +
                ", a.first_time \n" +
                ", a.next_time \n" +
                "from v${'$'}archived_log a \n" +
                "where trunc(a.first_time) = to_date(?) \n" +
                "order by a.first_time desc \n" +
                "fetch first row only"
        val preparedStatement: PreparedStatement = connect.prepareStatement(sql)
        preparedStatement.setDate(1, logDate)

        val resultSet: ResultSet = preparedStatement.executeQuery()
        resultSet.next()
        return LogFile(
                resultSet.getLong("RECID"),
                resultSet.getLong("STAMP"),
                resultSet.getString("NAME"),
                resultSet.getLong("FIRST_CHANGE#"),
                resultSet.getLong("NEXT_CHANGE#"),
                0,
                0
        )

    }

    fun operations(connect: Connection,
                   scn: Long,
                   rownum: Long): Collection<Operation> {

        val sql = "select \n" +
                "t.scn\n" +
                ",t.start_scn\n" +
                ",t.start_timestamp\n" +
                ",t.xid\n" +
                ",t.sql_redo\n" +
                ",t.sql_undo\n" +
                ",t.info\n" +
                ",t.redo_value\n" +
                " from V${'$'}LOGMNR_CONTENTS t\n" +
                " where rownum <= ?\n" +
                " and t.scn > ?"
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
                    resultSet.getString("REDO_VALUE")
            ))
        }
        return operationList
    }

    fun initialSCN(connect: Connection): Long {

        val sql = "select v.current_scn from v\$database v"
        val preparedStatement: PreparedStatement = connect.prepareStatement(sql)
        val resultSet: ResultSet = preparedStatement.executeQuery()

        resultSet.next()
        return resultSet.getLong("CURRENT_SCN")
    }
}