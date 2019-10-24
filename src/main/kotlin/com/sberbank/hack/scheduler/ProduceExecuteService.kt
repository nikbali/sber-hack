package com.sberbank.hack.scheduler

import com.sberbank.hack.dao.Select
import com.sberbank.hack.dao.models.Operation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.DriverManager
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.TimeUnit
import org.springframework.core.env.Environment
import java.sql.Connection
import java.util.concurrent.Callable
import java.util.concurrent.Executors


@Service
class ProduceExecuteService() {

    private val log = LoggerFactory.getLogger(ProduceExecuteService::class.java)

    @Autowired
    lateinit var environment: Environment

    @Autowired
    lateinit var select: Select

    private val logTaskQueue = ConcurrentLinkedQueue<Operation>();


    fun execute() {

        val connection = DriverManager.getConnection(
                environment.getProperty("spring.datasource.url"),
                environment.getProperty("spring.datasource.username"),
                environment.getProperty("spring.datasource.password"))

        val executor = Executors.newScheduledThreadPool(1)
        executor.submit(Consumer(connection));
    }

    internal inner class Consumer(private val connection: Connection) : Runnable {

        override fun run(){
            while (true) {
                logTaskQueue.addAll(select.operations(connection))
                log.info("add Logs in Queue")
            }
        }
    }

}
