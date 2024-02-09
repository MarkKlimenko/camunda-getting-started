package com.camunda.academy.insurance.worker.insurance.risk

import io.camunda.zeebe.spring.client.annotation.JobWorker
import io.camunda.zeebe.spring.client.annotation.Variable
import mu.KLogging
import org.springframework.stereotype.Component

@Component
class ReminderWorker() {

    @JobWorker(type = "insurance.risk.sendManagerReminder")
    fun sendManagerAlert(@Variable id: String) {
        logger.info { "Manager: Your operator is too slow. id=$id" }
    }

    @JobWorker(type = "insurance.risk.sendOperatorReminder")
    fun sendOperatorReminder(@Variable id: String) {
        logger.info { "Operator: You are too slow. dto=$id" }
    }

    private companion object : KLogging()
}