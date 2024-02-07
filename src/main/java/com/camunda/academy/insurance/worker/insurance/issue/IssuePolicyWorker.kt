package com.camunda.academy.insurance.worker.insurance.issue

import com.camunda.academy.insurance.dto.InsuranceDto
import com.camunda.academy.insurance.service.InsuranceService
import io.camunda.zeebe.spring.client.annotation.JobWorker
import io.camunda.zeebe.spring.client.annotation.VariablesAsType
import mu.KLogging
import org.springframework.stereotype.Component

@Component
class IssuePolicyWorker(
    val insuranceService: InsuranceService
) {

    @JobWorker(type = "insurance.issue.sendInitiatePayment")
    fun sendInitiatePayment(@VariablesAsType dto: InsuranceDto) {
        throw IllegalArgumentException("he he")
    }

    @JobWorker(type = "insurance.issuePolicy")
    fun issuePolicy(@VariablesAsType dto: InsuranceDto) {

    }

    @JobWorker(type = "insurance.sendPolicy")
    fun sendPolicy(@VariablesAsType dto: InsuranceDto) {
        logger.info { "User: Policy sent by email. dto=$dto" }
    }

    private companion object : KLogging()
}