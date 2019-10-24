package com.sberbank.hack.dao.models

data class LogFile(
        val recid: Long = 0,
        val stamp: Long = 0,
        val name: String = "",
        val firstChange: Long = 0,
        val nextChange: Long = 0,
        val firstTime: Long = 0,
        val nextTime: Long = 0
)