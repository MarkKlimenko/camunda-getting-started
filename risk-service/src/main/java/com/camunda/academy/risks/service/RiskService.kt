package com.camunda.academy.risks.service

import com.camunda.academy.risks.controller.dto.insurance.UniversalResponse
import com.camunda.academy.risks.dto.ManualDecision
import io.camunda.zeebe.client.ZeebeClient
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class RiskService(
    val client: ZeebeClient
) {

    suspend fun decideOnApplication(id: String, decision: ManualDecision): UniversalResponse {
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
