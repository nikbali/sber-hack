package com.sberbank.hack.dto

data class Transaction(
        val txId: Long,
        val instructions: List<Instruction>
)