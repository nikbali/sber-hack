package dao.models

import java.time.LocalDateTime

data class LogFile (

    var scn: Long = 0,
    var startScn: Long = 0,
    //TODO
    var startTimestamp: String = "",
    var xid: String = "",
    var sqlRedo : String = "",
    var sql_Undo : String?,
    var info : String?,
    var dataLgmr: String = ""


)