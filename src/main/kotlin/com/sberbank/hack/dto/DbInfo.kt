package com.sberbank.hack.dto

data class DbInfo(
        val connectionString: String,
        val username: String,
        val scanActive: Boolean
)