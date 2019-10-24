package com.sberbank.hack.scheduler

import com.sberbank.hack.dao.Select
import com.sberbank.hack.dao.models.Operation
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import java.sql.Connection
import java.sql.DriverManager
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
    lateinit var select: Select

    @Volatile private var  logTaskQueue = ConcurrentLinkedQueue<Operation>()

    fun enable() {
        isEnableProduce.set(true);
    }

    fun disable() {
        isEnableProduce.set(false);
    }

    fun execute() {
        val producer = Executors.newScheduledThreadPool(1)
        val consumer = Executors.newScheduledThreadPool(1)
        val connection = DriverManager.getConnection(
                environment.getProperty("spring.datasource.url"),
                environment.getProperty("spring.datasource.username"),
                environment.getProperty("spring.datasource.password"))

        producer.submit(Producer(connection))
        consumer.submit(Consumer(connection))
    }

    internal inner class Producer(private val connection: Connection) : Runnable {

        override fun run() {

            var currentScn : Long = 0

            while (true) {
                if (isEnableProduce.get()) {

                    val operations = select.operations(connection, currentScn, 10)

                    if(operations.isNotEmpty()){

                        logTaskQueue.addAll(operations)
                        log.info("add Logs in Queue")
                    }

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

                    if(!logTaskQueue.isEmpty()){
                        val operation: Operation = logTaskQueue.poll()
                        log.info("Write to file" + operation.xid)
                    }
                }

            }
        }
    }

}
