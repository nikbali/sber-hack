package com.sberbank.hack.dto

import lombok.Builder
import lombok.Data

@Data
@Builder
data class DbInfo(
        val connectionString: String,
        val username: String,
        val scanActive: Boolean
)