package com.camunda.academy.insurance.service

import com.camunda.academy.insurance.controller.dto.PaymentStatus
import com.camunda.academy.insurance.controller.dto.UniversalResponse
import io.camunda.zeebe.client.ZeebeClient
import org.springframework.stereotype.Service

@Service
class PaymentService(
    val client: ZeebeClient
) {

    suspend fun receivePayment(id: String, paymentStatus: PaymentStatus): UniversalResponse {
        client.newPublishMessageCommand()
            .messageName("insurance.issue.receivePayment")
            .correlationKey(id)
            .variables(mapOf("paymentStatus" to paymentStatus))
            .send()

        return UniversalResponse(
            id = id,
            message = "Payment processed"
        )
    }

    suspend fun receiveFundsReturning(id: String): UniversalResponse {
        client.newPublishMessageCommand()
            .messageName("insurance.reject.receiveFundsReturning")
            .correlationKey(id)
            .send()

        return UniversalResponse(
            id = id,
            message = "Payment returned"
        )
    }
}