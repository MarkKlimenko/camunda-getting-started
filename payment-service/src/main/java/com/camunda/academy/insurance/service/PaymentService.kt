package com.camunda.academy.insurance.service

import com.camunda.academy.insurance.controller.dto.UniversalResponse
import io.camunda.zeebe.client.ZeebeClient
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class PaymentService(
    val client: ZeebeClient
) {

    suspend fun callbackPayment(id: String): UniversalResponse {
        client.newPublishMessageCommand()
            .messageName("insurance.issue.paymentReceived")
            .correlationKey(id)
            .send()

        return UniversalResponse(
            id = id,
            message = "Payment callback received"
        )
    }

    private companion object : KLogging()
}