package com.sberbank.hack.scheduler

data class LMTask(
        val archLogFile: String,
        val startPosition: Long,
        val endPosition: Long
)