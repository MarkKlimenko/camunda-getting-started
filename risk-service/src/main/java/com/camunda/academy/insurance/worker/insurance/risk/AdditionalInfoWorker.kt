package com.camunda.academy.insurance.worker.insurance.risk

import com.camunda.academy.insurance.dto.InsuranceDto
import io.camunda.zeebe.spring.client.annotation.JobWorker
import io.camunda.zeebe.spring.client.annotation.VariablesAsType
import mu.KLogging
import org.springframework.stereotype.Component

@Component
class AdditionalInfoWorker {

    @JobWorker(type = "insurance.risk.sendUserAdditionalInfoRequest")
    fun sendUserAdditionalInfoRequest(@VariablesAsType dto: InsuranceDto) {
        logger.info { "User: We need more info for your request. dto=$dto" }
    }

    @JobWorker(type = "insurance.risk.sendUserAdditionalInfoReminder")
    fun sendUserAdditionalInfoReminder(@VariablesAsType dto: InsuranceDto) {
        logger.info { "User: Reminder. Please do not forget about additional information. dto=$dto" }
    }

    private companion object : KLogging()
}