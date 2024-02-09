package com.camunda.academy.insurance.service.risk

import com.camunda.academy.insurance.controller.dto.insurance.UniversalResponse
import com.camunda.academy.insurance.dto.ApplicationDecision
import io.camunda.zeebe.client.ZeebeClient
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class RiskService(
    val client: ZeebeClient
) {

    suspend fun decideOnApplication(id: String, decision: ApplicationDecision): UniversalResponse {
        client.newPublishMessageCommand()
            .messageName("insurance.risk.decideOnApplication")
            .correlationKey(id)
            .variables(mapOf("decision" to decision))
            .send()

        logger.info { "Operator: decided on application" }

        return UniversalResponse(
            id = id,
            message = "Application $decision"
        )
    }

    suspend fun addUserAdditionalInfo(id: String): UniversalResponse {
        client.newPublishMessageCommand()
            .messageName("insurance.risk.addUserAdditionalInfo")
            .correlationKey(id)
            .send()

        logger.info { "User: Info provided" }

        return UniversalResponse(
            id = id,
            message = "Additional information provided successfully"
        )
    }

    private companion object : KLogging()
}