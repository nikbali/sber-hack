package dao

import dao.models.LogFile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.util.*
import javax.sql.DataSource
import kotlin.collections.ArrayList

class Select {
    fun logFiles(connect: Connection) {
        val sql : String = "select * from v${'$'}archived_log where trunc(first_time) = to_date(sysdate)"
        val preparedStatement: PreparedStatement = connect.prepareStatement(sql)
        val resultSet: ResultSet = preparedStatement.executeQuery()
    }
    fun operations(connect: Connection): Collection<LogFile> {
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

        val logFileList = ArrayList<LogFile>()

        while(resultSet.next()) {
        logFileList.add(LogFile(
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
        return logFileList
    }
}