package com.camunda.academy.payment

import com.camunda.academy.payment.controller.dto.UniversalResponse
import com.camunda.academy.payment.persistence.entity.Payment
import com.camunda.academy.payment.persistence.entity.PaymentStatus
import com.camunda.academy.payment.util.await
import com.camunda.academy.payment.util.awaitClientRequest
import com.camunda.academy.payment.util.postStub
import io.camunda.zeebe.client.ZeebeClient
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID

class ProcessPaymentTest : WebIntegrationTest() {

    @Autowired
    lateinit var client: ZeebeClient

    @Test
    fun `Successfully process payment`() = runBlocking<Unit> {
        wireMockServer.postStub("/api/v1/payment.*")

        val externalId: String = UUID.randomUUID().toString()

        val response: UniversalResponse = webTestClient.post()
            .uri("/api/v1/payment/init/$externalId")
            .exchange()
            .expectStatus().isOk
            .returnResult(UniversalResponse::class.java)
            .responseBody.awaitSingle()

        assertEquals("payment initiated", response.message)

        // Callback: launch - Receive payment process
        webTestClient.post()
            .uri("/api/v1/payment/process/${response.id}")
            .exchange()
            .expectStatus().isOk

        // Check final payment status
        await {
            val finalResponse: Payment = webTestClient.get()
                .uri("/api/v1/payment/${response.id}")
                .exchange()
                .expectStatus().isOk
                .returnResult(Payment::class.java)
                .responseBody.awaitSingle()

            assertEquals(response.id, finalResponse.id)
            assertEquals(PaymentStatus.RECEIVED, finalResponse.status)
        }

        // await - Init payment return
        wireMockServer.awaitClientRequest("/api/v1/payment/$externalId/${PaymentStatus.RECEIVED}")
    }
}