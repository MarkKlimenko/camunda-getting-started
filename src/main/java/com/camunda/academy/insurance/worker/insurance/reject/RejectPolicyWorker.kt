package com.camunda.academy.insurance.worker.insurance.reject

import com.camunda.academy.insurance.dto.InsuranceDto
import com.camunda.academy.insurance.service.InsuranceService
import io.camunda.zeebe.spring.client.annotation.JobWorker
import io.camunda.zeebe.spring.client.annotation.VariablesAsType
import io.camunda.zeebe.spring.client.exception.ZeebeBpmnError
import mu.KLogging
import org.springframework.stereotype.Component

@Component
class RejectPolicyWorker(
    val insuranceService: InsuranceService
) {

    @JobWorker(type = "insurance.reject.returnFunds")
    fun returnFunds(@VariablesAsType dto: InsuranceDto) {
        if (dto.userAge == 11) {
            throw ZeebeBpmnError("returnFundsError", "Can not process this user age")
        }

        // TODO: check - if payment was processed
        logger.info { "Internal: Funds are returned. dto=$dto" }
    }

    @JobWorker(type = "insurance.reject.operatorAlertReturnFunds")
    fun operatorAlertReturnFunds(@VariablesAsType dto: InsuranceDto) {
        logger.info { "Operator: Problem during returning funds. dto=$dto" }
    }

    @JobWorker(type = "insurance.reject.rejectPolicy")
    fun rejectPolicy(@VariablesAsType dto: InsuranceDto) {
        // TODO: add queue
        logger.info { "Internal: Policy rejected. dto=$dto" }
    }

    @JobWorker(type = "insurance.reject.sendRejection")
    fun sendRejection(@VariablesAsType dto: InsuranceDto) {
        // TODO: add queue
        logger.info { "User: Rejection sent by email. dto=$dto" }
    }

    private companion object : KLogging()
}