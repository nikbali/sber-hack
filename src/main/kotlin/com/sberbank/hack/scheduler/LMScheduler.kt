package com.sberbank.hack.scheduler

import com.sberbank.hack.dto.DbInfo
import java.util.concurrent.ConcurrentLinkedQueue

class LMScheduler(val threadCount: Int = 2, val dbInfo: DbInfo) {
    val queue = ConcurrentLinkedQueue<LMTask>()
    val chunkSize: Long = 20 //FIXME which size

    fun planArchLogToProcess(task: LMTask) {
        for (x in task.startPosition..task.endPosition step chunkSize) {
            val stepEndPosition = if (x + chunkSize > task.endPosition) task.endPosition else x + chunkSize
            queue.add(LMTask(task.archLogFile, x, stepEndPosition))
        }
    }
}