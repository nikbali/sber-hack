package com.sberbank.hack.filewriter

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.sberbank.hack.dto.Transaction
import org.springframework.stereotype.Service

@Service
class LogService {
    fun log(tx: Transaction) {
        val mapper = jacksonObjectMapper()
        val json = mapper.writeValueAsString(tx)
        FileWrite.writeLog(json)
    }
}