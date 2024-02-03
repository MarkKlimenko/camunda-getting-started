package com.camunda.academy.insurance.worker

import com.camunda.academy.insurance.dto.InsuranceDto
import com.camunda.academy.insurance.service.InsuranceService
import io.camunda.zeebe.spring.client.annotation.JobWorker
import io.camunda.zeebe.spring.client.annotation.VariablesAsType
import mu.KLogging
import org.springframework.stereotype.Component

@Component
class RejectPolicyWorker(
    val insuranceService: InsuranceService
) {

    @JobWorker(type = "insurance.rejectPolicy")
    fun rejectPolicy(@VariablesAsType dto: InsuranceDto) {

    }

    @JobWorker(type = "insurance.sendRejection")
    fun sendRejection(@VariablesAsType dto: InsuranceDto) {
        logger.info { "User: Rejection sent by email. dto=$dto" }
    }

    private companion object : KLogging()
}