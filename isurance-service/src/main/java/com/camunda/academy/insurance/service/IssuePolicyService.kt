package com.camunda.academy.insurance.service

import com.camunda.academy.insurance.controller.dto.ManualPolicyStatus
import com.camunda.academy.insurance.controller.dto.UniversalResponse
import com.camunda.academy.insurance.persistence.entity.Insurance
import com.camunda.academy.insurance.persistence.entity.InsuranceStatus
import com.camunda.academy.insurance.persistence.repository.InsuranceRepository
import io.camunda.zeebe.client.ZeebeClient
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class IssuePolicyService(
    val client: ZeebeClient,
    val insuranceRepository: InsuranceRepository,
) {
    suspend fun retryIssuePolicy(id: String, status: ManualPolicyStatus): UniversalResponse {
        val insurance: Insurance = insuranceRepository.findById(id)
            ?: error("Insurance not found")

        if (status == ManualPolicyStatus.RETRY) {
            insuranceRepository.save(insurance.copy(status = InsuranceStatus.PENDING))
        } else if (status == ManualPolicyStatus.APPROVED) {
            insuranceRepository.save(insurance.copy(status = InsuranceStatus.SUCCESS))
        }

        client.newPublishMessageCommand()
            .messageName("insurance.issue.retryIssuePolicy")
            .correlationKey(id)
            .variables(mapOf("status" to status))
            .send()

        logger.info { "Operator: retried issue policy with status $status" }

        return UniversalResponse(
            id = id,
            message = "Retry issue $status"
        )
    }

    private companion object : KLogging()
}
