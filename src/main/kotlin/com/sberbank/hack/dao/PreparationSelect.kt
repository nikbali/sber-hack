package com.sberbank.hack.dao

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*
import kotlin.system.exitProcess

object PreparationSelect {
    fun build(connect: Connection) {
        val sql = "begin DBMS_LOGMNR_D.BUILD( OPTIONS=> DBMS_LOGMNR_D.STORE_IN_REDO_LOGS); end;"
        val prepareCall = connect.prepareCall(sql)
        prepareCall.execute()
    }
    fun addLogFile(connect: Connection, logFileName: String) {
        val sql = """begin DBMS_LOGMNR.ADD_LOGFILE(  LOGFILENAME => ?,  OPTIONS => DBMS_LOGMNR.NEW); end;"""
        val preparedCall = connect.prepareCall(sql)
        preparedCall.setString(1, logFileName)
        preparedCall.execute();
    }

    fun startLogMnr(connect: Connection) {
        val sql = """begin DBMS_LOGMNR.START_LOGMNR( OPTIONS  => DBMS_LOGMNR.DICT_FROM_REDO_LOGS + DBMS_LOGMNR.COMMITTED_DATA_ONLY +  DBMS_LOGMNR.PRINT_PRETTY_SQL +  DBMS_LOGMNR.CONTINUOUS_MINE); end;"""
        val prepareCall = connect.prepareCall(sql)
        prepareCall.execute()
    }

    fun endLogMnr(connect: Connection) {
        val sql = """begin DBMS_LOGMNR.END_LOGMNR(); end;"""
        val prepareCall = connect.prepareCall(sql)
        prepareCall.execute()
    }
}

fun main() {
    Locale.setDefault(Locale.ENGLISH)
    val url = "jdbc:oracle:thin:@172.30.13.84:1521/orclcdb.localdomain"

    var connection: Connection? = null
    try {
        connection = DriverManager.getConnection(url, "sergonas", "password")
        println(System.currentTimeMillis())
        PreparationSelect.build(connection)
        println(System.currentTimeMillis())
        PreparationSelect.addLogFile(connection, "/u03/app/oracle/fast_recovery_area/ORCLCDB/archivelog/2019_10_24/o1_mf_1_1471_gv48y7hj_.arc")
        println(System.currentTimeMillis())
        PreparationSelect.startLogMnr(connection)
        println(System.currentTimeMillis())
        for (x in Select().operations(connection, 0, 20)) {
            println(x)
        }
        println(System.currentTimeMillis())
        PreparationSelect.endLogMnr(connection)
    } catch (e: SQLException) {
        println("Connection Failed : " + e.message)
        exitProcess(-1)
    }

    connection.close()
}