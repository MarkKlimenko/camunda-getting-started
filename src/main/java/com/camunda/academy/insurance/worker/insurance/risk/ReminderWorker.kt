package com.camunda.academy.insurance.worker.insurance.risk

import com.camunda.academy.insurance.dto.InsuranceDto
import com.camunda.academy.insurance.service.InsuranceService
import io.camunda.zeebe.spring.client.annotation.JobWorker
import io.camunda.zeebe.spring.client.annotation.VariablesAsType
import mu.KLogging
import org.springframework.stereotype.Component

@Component
class ReminderWorker(
    val insuranceService: InsuranceService
) {

    @JobWorker(type = "insurance.risk.sendManagerReminder")
    fun sendManagerAlert(@VariablesAsType dto: InsuranceDto) {
        logger.info { "Manager: Your operator is too slow. dto=$dto" }
    }

    @JobWorker(type = "insurance.risk.sendOperatorReminder")
    fun sendOperatorReminder(@VariablesAsType dto: InsuranceDto) {
        logger.info { "Operator: You are too slow. dto=$dto" }
    }

    private companion object : KLogging()
}