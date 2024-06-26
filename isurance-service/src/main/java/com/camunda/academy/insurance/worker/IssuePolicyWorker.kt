package com.camunda.academy.insurance.worker

import com.camunda.academy.insurance.persistence.entity.Insurance
import com.camunda.academy.insurance.persistence.entity.InsuranceStatus
import com.camunda.academy.insurance.persistence.entity.InsuranceStatus.PENDING_MANUAL
import com.camunda.academy.insurance.persistence.entity.InsuranceStatus.REJECTED
import com.camunda.academy.insurance.persistence.entity.InsuranceStatus.SUCCESS
import com.camunda.academy.insurance.persistence.repository.InsuranceRepository
import io.camunda.zeebe.spring.client.annotation.JobWorker
import io.camunda.zeebe.spring.client.annotation.Variable
import io.camunda.zeebe.spring.client.exception.ZeebeBpmnError
import kotlinx.coroutines.runBlocking
import mu.KLogging
import org.springframework.stereotype.Component

@Component
class IssuePolicyWorker(
    val insuranceRepository: InsuranceRepository,
) {

    @JobWorker(type = "insurance.issue.sendInitiatePayment")
    fun sendInitiatePayment(@Variable id: String) {
        logger.info { "User: Payment initiated. Payment link sent to user. id=$id" }
    }

    @JobWorker(type = "insurance.issue.paymentReminder")
    fun paymentReminder(@Variable id: String) {
        logger.info { "User: Don't forget to pay for the policy. id=$id" }
    }

    @JobWorker(type = "insurance.issue.issuePolicy")
    fun issuePolicy(@Variable id: String): Map<String, InsuranceStatus> = runBlocking {
        val insurance: Insurance = insuranceRepository.findById(id)
            ?: error("Insurance not found")

        try {
            when (insurance.userAge) {
                31 -> throw IllegalArgumentException("Age 31 is not supported")

                32 -> {
                    insuranceRepository.save(insurance.copy(status = REJECTED))

                    logger.info { "System: Policy rejected. id=$id" }
                    mapOf("status" to REJECTED)
                }

                else -> {
                    insuranceRepository.save(insurance.copy(status = SUCCESS))

                    logger.info { "System: Policy issued. id=$id" }
                    mapOf("status" to SUCCESS)
                }
            }

        } catch (e: Exception) {
            logger.error(e) { "Error during issuePolicy process" }
            throw ZeebeBpmnError("issuePolicyError", e.message)
        }
    }

    @JobWorker(type = "insurance.issue.notifyOperatorOnError")
    fun notifyOperatorOnError(@Variable id: String) = runBlocking {
        val insurance: Insurance = insuranceRepository.findById(id)
            ?.also { insuranceRepository.save(it.copy(status = PENDING_MANUAL)) }
            ?: error("Insurance not found")

        logger.info { "Operator: Error during policy issuing. id=$insurance.id" }
    }

    @JobWorker(type = "insurance.issue.sendPolicy")
    fun sendPolicy(@Variable id: String) {
        logger.info { "User: Policy sent by email. id=$id" }
    }

    private companion object : KLogging()
}
