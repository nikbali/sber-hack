package com.sberbank.hack.scheduler

import java.util.*
import java.util.concurrent.CompletableFuture

class LMWorkPlanner(val queue: Queue<LMTask>) : Runnable {
    override fun run() {
        val task = queue.poll()
        if (task == null) {
            Thread.sleep(1000)
        }


        val future = CompletableFuture.supplyAsync<Result>({
            process(task)
        })
    }
}
data class Result(val sql: String)

fun process(task: LMTask): Result = TODO()