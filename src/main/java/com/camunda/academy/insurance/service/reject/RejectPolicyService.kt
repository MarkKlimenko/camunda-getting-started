package com.camunda.academy.insurance.service.reject

import com.camunda.academy.insurance.controller.dto.insurance.UniversalResponse
import io.camunda.zeebe.client.ZeebeClient
import org.springframework.stereotype.Service

@Service
class RejectPolicyService(
    val client: ZeebeClient
) {

    fun operatorReturnFunds(id: String): UniversalResponse {
        client.newPublishMessageCommand()
            .messageName("insurance.reject.operatorReturnFunds")
            .correlationKey(id)
            .send()

        return UniversalResponse(
            id = id,
            message = "Funds were successfully returned to user"
        )
    }
}