package com.camunda.academy.payment.util

import kotlinx.coroutines.runBlocking
import org.awaitility.Awaitility
import org.opentest4j.AssertionFailedError
import java.time.Duration
import java.util.concurrent.TimeUnit

fun await(action: suspend () -> Unit) {
    Awaitility.waitAtMost(Duration.ofSeconds(10))
        .pollInterval(500, TimeUnit.MILLISECONDS)
        .until {
            try {
                runBlocking {
                    action.invoke()
                }
                true
            } catch (e: AssertionFailedError) {
                println(e)
                false
            }
        }
}