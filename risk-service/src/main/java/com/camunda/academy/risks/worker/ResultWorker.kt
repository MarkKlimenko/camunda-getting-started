package com.camunda.academy.risks.worker

import io.camunda.zeebe.spring.client.annotation.JobWorker
import io.camunda.zeebe.spring.client.annotation.Variable
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component

@Component
class ResultWorker {

    @JobWorker(type = "risk.determine.sendRiskResult")
    fun sendRiskResult(@Variable id: String) = runBlocking<Unit> {
        throw NotImplementedError()
    }
}
