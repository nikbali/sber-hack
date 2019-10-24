package dao

import dao.models.LogFile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import javax.sql.DataSource

class Select {
    fun logFiles(connect: Connection) {
        val sql : String = "select * from v${'$'}archived_log where trunc(first_time) = to_date(sysdate)"
        val preparedStatement: PreparedStatement = connect.prepareStatement(sql)
        val resultSet: ResultSet = preparedStatement.executeQuery()
    }
    fun operations(connect: Connection): LogFile {
        //V$LOGMNR_CONTENTS
        val sql : String = "select " +
                "t.scn" +
                ",t.start_scn" +
                ",t.start_timestamp" +
                ",t.xid" +
                ",t.sql_redo" +
                ",t.sql_undo" +
                ",t.info" +
                ",t.redo_value" +
                ",t.xid" +
                " from data_lgmr t"
        val preparedStatement: PreparedStatement = connect.prepareStatement(sql)
        val resultSet: ResultSet = preparedStatement.executeQuery()

        return LogFile(
                resultSet.getLong("SCN"),
                resultSet.getLong("START_SCN"),
                resultSet.getString("START_TIMESTAMP"),
                resultSet.getString("xid"),
                resultSet.getString("sql_redo"),
                resultSet.getString("sql_undo"),
                resultSet.getString("redo_value"),
                resultSet.getString("xid")
        )
    }
}