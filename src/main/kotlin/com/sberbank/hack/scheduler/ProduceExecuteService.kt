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
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors
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

    @Volatile
    private var logTaskQueue = ConcurrentLinkedQueue<Operation>()

    fun enable() {
        isEnableProduce.set(true);
    }

    fun disable() {
        isEnableProduce.set(false);
    }

    fun execute() {
        Locale.setDefault(Locale.ENGLISH)
        val producer = Executors.newScheduledThreadPool(1)
        val consumer = Executors.newScheduledThreadPool(1)
        val connection = DriverManager.getConnection(
                environment.getProperty("spring.datasource.url"),
                environment.getProperty("spring.datasource.username"),
                environment.getProperty("spring.datasource.password"))

        PreparationSelect.build(connection)
        val logFiles = select.logFiles(connection, Date(System.currentTimeMillis() - 100000))
//        val scn = select.initialSCN(connection)

        PreparationSelect.addLogFile(connection, logFiles.name)
        PreparationSelect.startLogMnr(connection)

        producer.submit(Producer(connection))
        consumer.submit(Consumer(connection))
    }

    internal inner class Producer(private val connection: Connection) : Runnable {

        override fun run() {

            var currentScn : Long = FileWriter.readCdn().cdn

            if(currentScn == 0L){
                currentScn = select.initialSCN(connection)
            }

            while (true) {
                if (isEnableProduce.get()) {

                    val operations = select.operations(connection, currentScn, 10)

                    if (operations.isNotEmpty()) {
                        val lastOperation: Operation? = getLastElement(operations)
                        currentScn = lastOperation?.scn ?: currentScn
                        logTaskQueue.addAll(operations)
                        log.info("add Logs in Queue")
                    } else {
                        log.info("data empty")
                        Thread.sleep(1000)
                    }

                } else {
                    //записываем последний cnd
                   FileWriter.writeCdn(CdnDto(currentScn))
                }
            }
        }
    }

    internal inner class Consumer(private val connection: Connection) : Runnable {

        override fun run() {

            while (true) {

                if (!isEnableProduce.get() && logTaskQueue.isEmpty()) {

                    //завершаем поток и закрываем конект
                    connection.close()
                    log.info("End FIFO service")
                    break;

                } else {

                    if (!logTaskQueue.isEmpty()) {
                        val operation: Operation = logTaskQueue.poll()
                        log.info(operation.toString())
                        logService.log(operation)
                        log.info("Write to file" + operation.xid)
                    }
                }
            }
        }
    }

    fun <T> getLastElement(elements: Iterable<T>): T? {
        var lastElement: T? = null

        for (element in elements) {
            lastElement = element
        }

        return lastElement
    }
}
