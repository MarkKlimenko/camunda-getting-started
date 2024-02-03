package com.camunda.academy.insurance.worker

import com.camunda.academy.insurance.dto.InsuranceDto
import com.camunda.academy.insurance.service.InsuranceService
import io.camunda.zeebe.spring.client.annotation.JobWorker
import io.camunda.zeebe.spring.client.annotation.VariablesAsType
import mu.KLogging
import org.springframework.stereotype.Component

@Component
class OperatorSendAlertWorker(
    val insuranceService: InsuranceService
) {

    @JobWorker(type = "insurance.sendOperatorAlert")
    fun issuePolicy(@VariablesAsType dto: InsuranceDto) {

        logger.info { "Operator: Please, make decision. dto=$dto" }
    }

    private companion object : KLogging()
}