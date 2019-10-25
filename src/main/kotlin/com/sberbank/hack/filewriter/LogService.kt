package com.sberbank.hack.filewriter

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.common.collect.EvictingQueue
import com.sberbank.hack.dao.models.Operation
import org.springframework.stereotype.Service
import java.util.*

@Service
class LogService {

    private val buffer = EvictingQueue.create<Operation>(20)

    fun getLast(): Collection<Operation> = Collections.unmodifiableCollection(buffer)

    fun log(operation: Operation) {

        val mapper = jacksonObjectMapper()
        val json = mapper.writeValueAsString(operation)
        //FileWriter.writeInstruction(it.sql, tx.txId)}
        FileWriter.writeLog(json)
        buffer.add(operation)
    }
}