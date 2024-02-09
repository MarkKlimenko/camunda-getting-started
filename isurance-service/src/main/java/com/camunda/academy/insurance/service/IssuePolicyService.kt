package com.camunda.academy.insurance.service

import com.camunda.academy.insurance.controller.dto.insurance.UniversalResponse
import com.camunda.academy.insurance.dto.InsuranceDto
import io.camunda.zeebe.client.ZeebeClient
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class IssuePolicyService(
    val client: ZeebeClient
) {
    suspend fun retryIssuePolicy(id: String, status: InsuranceDto.Status): UniversalResponse {
        //val resultDto: InsuranceDto = dto.copy(status = status)
        //insuranceService.save(resultDto)

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