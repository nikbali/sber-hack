package com.sberbank.hack.dao.models

data class Operation(

        val scn: Long = 0,
        val startScn: Long? = 0,
        val startTimestamp: String? = "",
        val xid: String? = "",
        val sqlRedo: String? = "",
        val sql_Undo: String?,
        val info: String?
)