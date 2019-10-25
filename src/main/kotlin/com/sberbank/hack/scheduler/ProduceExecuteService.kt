package com.sberbank.hack.scheduler

import com.sberbank.hack.dao.PreparationSelect
import com.sberbank.hack.dao.Select
import com.sberbank.hack.dao.models.Operation
import com.sberbank.hack.dto.CdnDto
import com.sberbank.hack.filewriter.FileWriter
import com.sberbank.hack.filewriter.LogService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import java.sql.Connection
import java.sql.Date
import java.sql.DriverManager
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Этот прекрасный сервис в отдельном потоке вытаскивает логи из вьюъи и кладет в очередь,
 * также паралельно работает еще один поток, который пишет в файл из очереди
 * Имееется возможность включения/отключения сервиса, но при выключении поток на запись в файл продолжит работать пока очердь иммеет информацию
 */
@Service
class ProduceExecuteService {

    private val log = LoggerFactory.getLogger(ProduceExecuteService::class.java)

    private val isEnableProduce: AtomicBoolean = AtomicBoolean(false)

    @Autowired
    lateinit var environment: Environment

    @Autowired
    lateinit var logService: LogService

    @Autowired
    lateinit var select: Select

    fun enable() {
        isEnableProduce.set(true);
    }

    fun disable() {
        isEnableProduce.set(false);
    }

    fun execute() {
        CompletableFuture.runAsync {
            Locale.setDefault(Locale.ENGLISH)
            val connection = DriverManager.getConnection(
                    environment.getProperty("spring.datasource.url"),
                    environment.getProperty("spring.datasource.username"),
                    environment.getProperty("spring.datasource.password"))

            log.info("Dict build started")
            PreparationSelect.build(connection)
            log.info("Dict dict finished")
            val logFile = select.logFiles(connection, Date(System.currentTimeMillis() - 100000))
            log.info("Arch log file ${logFile.name}")
            log.info("Add log file started")
            PreparationSelect.addLogFile(connection, logFile.name)
            log.info("Start log miner")
            PreparationSelect.startLogMnr(connection)
            log.info("Log miner started")
            doWork(connection)
        }
    }

    fun doWork(conn: Connection) {
        var currentScn : Long = FileWriter.readCdn().cdn

        if(currentScn == 0L) {
            currentScn = select.initialSCN(conn)
        }
        log.info("Current scn = $currentScn")

        while (true) {
            if (isEnableProduce.get()) {
                log.info("Reading mined data")
                val operations = select.operations(conn, currentScn, 10)

                if (operations.isNotEmpty()) {
                    val lastOperation: Operation? = getLastElement(operations)
                    currentScn = lastOperation?.scn ?: currentScn
                    for (op in operations) {
                        log.info(op.toString())
                        logService.log(op)
                        log.info("Write to file tx with xid" + op.xid)
                    }
                    log.info("add Logs in Queue")
                } else {
                    log.info("data empty. sleeping")
                    Thread.sleep(1000)
                }
            } else {
                //записываем последний cnd
                FileWriter.writeCdn(CdnDto(currentScn))
                log.info("Written current scn = $currentScn")
            }
        }
    }

    private fun <T> getLastElement(elements: Iterable<T>): T? {
        var lastElement: T? = null

        for (element in elements) {
            lastElement = element
        }

        return lastElement
    }
}
