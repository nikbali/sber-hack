package com.sberbank.hack.dao

import com.sberbank.hack.dao.models.LogFile
import com.sberbank.hack.dao.models.Operation
import org.springframework.stereotype.Component
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import kotlin.collections.ArrayList

@Component
class Select {
    fun logFiles(connect: Connection): Collection<LogFile> {
        val sql: String = "select\n" +
                "a.recid\n" +
                ",a.stamp\n" +
                ",a.name\n" +
                ",a.first_change#\n" +
                ",a.next_change#\n" +
                ",a.first_time\n" +
                ",a.next_time" +
                "from v${'$'}archived_log a \n" +
                "where trunc(a.first_time) = to_date(sysdate)\n"
        val preparedStatement: PreparedStatement = connect.prepareStatement(sql)
        val resultSet: ResultSet = preparedStatement.executeQuery()

        val logFileList = ArrayList<LogFile>()

        while (resultSet.next()) {
            logFileList.add(LogFile(
                    resultSet.getLong("RECID"),
                    resultSet.getLong("STAMP"),
                    resultSet.getString("NAME"),
                    resultSet.getLong("FIRST_CHANGE#"),
                    resultSet.getLong("NEXT_CHANGE#"),
                    resultSet.getLong("FIRST_TIME"),
                    resultSet.getLong("NEXT_TIME")
            ))
        }
        return logFileList
    }

    fun operations(connect: Connection): Collection<Operation> {
        //V$LOGMNR_CONTENTS
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
                //TODO
                " where rownum < 11"
        val preparedStatement: PreparedStatement = connect.prepareStatement(sql)
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