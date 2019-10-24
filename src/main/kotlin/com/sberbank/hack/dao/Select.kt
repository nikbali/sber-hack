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
        val sql : String = "select * from v${'$'}archived_log where trunc(first_time) = to_date(sysdate)"
        val preparedStatement: PreparedStatement = connect.prepareStatement(sql)
        val resultSet: ResultSet = preparedStatement.executeQuery()

        val logFileList = ArrayList<LogFile>()

/*        while(resultSet.next()) {
            logFileList.add(Operation(
                    resultSet.getLong("SCN"),
                    resultSet.getLong("START_SCN"),
                    resultSet.getString("START_TIMESTAMP"),
                    resultSet.getString("XID"),
                    resultSet.getString("SQL_REDO"),
                    resultSet.getString("SQL_UNDO"),
                    resultSet.getString("REDO_VALUE"),
                    resultSet.getString("XID")
            ))
        }*/
        return logFileList
    }
    fun operations(connect: Connection): Collection<Operation> {
        //V$LOGMNR_CONTENTS
        val sql: String = "select " +
                "t.scn" +
                ",t.start_scn" +
                ",t.start_timestamp" +
                ",t.xid" +
                ",t.sql_redo" +
                ",t.sql_undo" +
                ",t.info" +
                ",t.redo_value" +
                ",t.xid" +
                " from data_lgmr t" +
                //TODO
                " where rownum < 11"
        val preparedStatement: PreparedStatement = connect.prepareStatement(sql)
        val resultSet: ResultSet = preparedStatement.executeQuery()

        val operationList = ArrayList<Operation>()

        while(resultSet.next()) {
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