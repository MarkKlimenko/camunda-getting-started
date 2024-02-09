package com.camunda.academy.insurance.worker.insurance.risk

import io.camunda.zeebe.spring.client.annotation.JobWorker
import io.camunda.zeebe.spring.client.annotation.Variable
import mu.KLogging
import org.springframework.stereotype.Component

@Component
class OperatorSendAlertWorker() {

    @JobWorker(type = "insurance.risk.sendOperatorAlert")
    fun sendOperatorAlert(@Variable id: String) {
        logger.info { "Operator: Please, make decision. id=$id" }
    }

    private companion object : KLogging()
}