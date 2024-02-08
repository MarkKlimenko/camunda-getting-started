package com.camunda.academy.insurance.worker.insurance.issue

import com.camunda.academy.insurance.dto.InsuranceDto
import com.camunda.academy.insurance.service.InsuranceService
import io.camunda.zeebe.spring.client.annotation.JobWorker
import io.camunda.zeebe.spring.client.annotation.VariablesAsType
import io.camunda.zeebe.spring.client.exception.ZeebeBpmnError
import mu.KLogging
import org.springframework.stereotype.Component

@Component
class IssuePolicyWorker(
    val insuranceService: InsuranceService
) {

    @JobWorker(type = "insurance.issue.sendInitiatePayment")
    fun sendInitiatePayment(@VariablesAsType dto: InsuranceDto) {
        logger.info { "User: Payment initiated. Payment link sent to user. dto=$dto" }
    }

    @JobWorker(type = "insurance.issue.paymentReminder")
    fun paymentReminder(@VariablesAsType dto: InsuranceDto) {
        logger.info { "User: Don't forget to pay for the policy. dto=$dto" }
    }

    @JobWorker(type = "insurance.issue.issuePolicy")
    fun issuePolicy(@VariablesAsType dto: InsuranceDto): InsuranceDto {
        try {
            return if (dto.userAge == 31) {
                throw IllegalArgumentException("Age 31 is not supported")
            } else if (dto.userAge == 32) {
                val resultDto: InsuranceDto = dto.copy(status = InsuranceDto.Status.REJECTED)
                insuranceService.save(resultDto)

                logger.info { "System: Policy rejected. dto=$dto" }
                resultDto
            } else {
                val resultDto: InsuranceDto = dto.copy(status = InsuranceDto.Status.SUCCESS)
                insuranceService.save(resultDto)

                logger.info { "System: Policy issued. dto=$dto" }
                resultDto
            }

        } catch (e: Exception) {
            logger.error(e) { "Error during issuePolicy process" }
            throw ZeebeBpmnError("issuePolicyError", e.message)
        }
    }

    @JobWorker(type = "insurance.issue.notifyOperatorOnError")
    fun notifyOperatorOnError(@VariablesAsType dto: InsuranceDto) {
        logger.info { "Operator: Error during policy issuing. dto=$dto" }
    }

    @JobWorker(type = "insurance.issue.sendPolicy")
    fun sendPolicy(@VariablesAsType dto: InsuranceDto) {
        logger.info { "User: Policy sent by email. dto=$dto" }
    }

    private companion object : KLogging()
}