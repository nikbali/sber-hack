package com.sberbank.hack.scheduler

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.sberbank.hack.dto.Transaction
import com.sberbank.hack.filewriter.FileWrite
import dao.Select
import dao.models.LogFile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.sql.Connection
import java.sql.DriverManager
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.ScheduledFuture
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Producer
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService


@Service
class ProduceExecuteService() {


    @Value("#{spring.datasource.url}")
    lateinit var url: String


    @Value("#{spring.datasource.username}")
    lateinit var username: String


    @Value("#{spring.datasource.password}")
    lateinit var password: String

    @Autowired
    lateinit var select: Select

    private val logTaskQueue = ConcurrentLinkedQueue<LogFile>();


    fun execute() {

        val executor = Executors.newScheduledThreadPool(1)

        while (true) {
            executor.schedule(Consumer(), 10, TimeUnit.SECONDS)
        }

    }

    internal inner class Consumer : Thread() {

        override fun run() {
            val connection = DriverManager.getConnection(url, username, password)
            logTaskQueue.addAll(select.operations(connection))
        }
    }

}
