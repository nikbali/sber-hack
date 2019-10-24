package com.sberbank.hack.dto

import java.time.Instant

data class Instruction(
        val sql: String,
        val undoSql: String,
        val ts: Instant
//additional info
)
