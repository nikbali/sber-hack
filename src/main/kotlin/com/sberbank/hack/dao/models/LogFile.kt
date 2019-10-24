package com.sberbank.hack.dao.models

data class LogFile(
        var recid: Long = 0,
        var stamp: Long = 0,
        var name: String = "",
        var firstChange: Long = 0,
        var nextChange: Long = 0,
        var firstTime: Long = 0,
        var nextTime: Long = 0
)