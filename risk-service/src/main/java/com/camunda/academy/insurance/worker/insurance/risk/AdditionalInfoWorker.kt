package com.camunda.academy.insurance.worker.insurance.risk

import io.camunda.zeebe.spring.client.annotation.JobWorker
import io.camunda.zeebe.spring.client.annotation.Variable
import mu.KLogging
import org.springframework.stereotype.Component

@Component
class AdditionalInfoWorker {

    @JobWorker(type = "insurance.risk.sendUserAdditionalInfoRequest")
    fun sendUserAdditionalInfoRequest(@Variable id: String) {
        logger.info { "User: We need more info for your request. id=$id" }
    }

    @JobWorker(type = "insurance.risk.sendUserAdditionalInfoReminder")
    fun sendUserAdditionalInfoReminder(@Variable id: String) {
        logger.info { "User: Reminder. Please do not forget about additional information. id=$id" }
    }

    private companion object : KLogging()
}