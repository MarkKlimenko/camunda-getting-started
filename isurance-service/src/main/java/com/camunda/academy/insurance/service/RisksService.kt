package com.camunda.academy.insurance.service

import com.camunda.academy.insurance.controller.dto.RisksDecisionStatus
import com.camunda.academy.insurance.controller.dto.UniversalResponse
import io.camunda.zeebe.client.ZeebeClient
import org.springframework.stereotype.Service

@Service
class RisksService(
    val client: ZeebeClient
) {

    suspend fun processRisksDecision(id: String, risksDecision: RisksDecisionStatus): UniversalResponse {
        client.newPublishMessageCommand()
            .messageName("insurance.receiveRisksDecision")
            .correlationKey(id)
            .variables(mapOf("risksDecision" to risksDecision))
            .send()

        return UniversalResponse(
            id = id,
            message = "Risks decision processed"
        )
    }
}