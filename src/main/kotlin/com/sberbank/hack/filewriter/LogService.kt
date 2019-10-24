package com.sberbank.hack.filewriter

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.common.collect.EvictingQueue
import com.sberbank.hack.dto.Transaction
import org.springframework.stereotype.Service
import java.util.*

@Service
class LogService {
    private val buffer = EvictingQueue.create<Transaction>(20)

    fun getLast(): Collection<Transaction> = Collections.unmodifiableCollection(buffer)

    fun log(tx: Transaction) {
        val mapper = jacksonObjectMapper()
        val json = mapper.writeValueAsString(tx)
        tx.instructions.forEach{FileWrite.writeInstruction(it.sql, tx.txId)}
        FileWrite.writeLog(json)
        buffer.add(tx)
    }
}