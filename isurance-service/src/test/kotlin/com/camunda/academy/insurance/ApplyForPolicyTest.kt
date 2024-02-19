package com.camunda.academy.insurance

import com.camunda.academy.insurance.controller.dto.CreateInsuranceRequest
import com.camunda.academy.insurance.controller.dto.PaymentStatus
import com.camunda.academy.insurance.controller.dto.RisksDecisionStatus
import com.camunda.academy.insurance.controller.dto.UniversalResponse
import com.camunda.academy.insurance.persistence.entity.Insurance
import com.camunda.academy.insurance.persistence.entity.InsuranceStatus
import com.camunda.academy.insurance.util.await
import com.camunda.academy.insurance.util.awaitClientRequest
import com.camunda.academy.insurance.util.postStub
import io.camunda.zeebe.client.ZeebeClient
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType

class ApplyForPolicyTest : WebIntegrationTest() {

    @Autowired
    lateinit var client: ZeebeClient

    @Test
    fun `Success flow test`() = runBlocking<Unit> {
        wireMockServer.postStub("/api/v1/processing.*")
        wireMockServer.postStub("/api/v1/payment/init.*")


        val request = CreateInsuranceRequest(
            userName = "name",
            userAge = 30,
            autoBrand = "audi",
        )

        val response: UniversalResponse = webTestClient.post()
            .uri("/api/v1/insurance")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk
            .returnResult(UniversalResponse::class.java)
            .responseBody.awaitSingle()

        assertEquals("Your application was created. Please wait for decision", response.message)

        // await for worker - Init risks processing
        wireMockServer.awaitClientRequest("/api/v1/processing/${response.id}")

        // Callback: launch - Receive risks decision
        webTestClient.post()
            .uri("/api/v1/risks/${response.id}/${RisksDecisionStatus.ACCEPTED}")
            .exchange()
            .expectStatus().isOk
            .returnResult(UniversalResponse::class.java)
            .responseBody.awaitSingle()

        // await - Init payment process
        wireMockServer.awaitClientRequest("/api/v1/payment/init/${response.id}")

        // Callback: launch - Receive payment status
        webTestClient.post()
            .uri("/api/v1/payment/${response.id}/${PaymentStatus.RECEIVED}")
            .exchange()
            .expectStatus().isOk
            .returnResult(UniversalResponse::class.java)
            .responseBody.awaitSingle()

        // Check final insurance status
        await {
            val finalResponse: Insurance = webTestClient.get()
                .uri("/api/v1/insurance/${response.id}")
                .exchange()
                .expectStatus().isOk
                .returnResult(Insurance::class.java)
                .responseBody.awaitSingle()

            assertEquals(response.id, finalResponse.id)
            assertEquals(InsuranceStatus.SUCCESS, finalResponse.status)
        }
    }

    @Test
    fun `Reject policy by risk decision`() = runBlocking<Unit> {
        wireMockServer.postStub("/api/v1/processing.*")
        wireMockServer.postStub("/api/v1/payment/return.*")


        val request = CreateInsuranceRequest(
            userName = "Name Rejected",
            userAge = 30,
            autoBrand = "audi",
        )

        val response: UniversalResponse = webTestClient.post()
            .uri("/api/v1/insurance")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk
            .returnResult(UniversalResponse::class.java)
            .responseBody.awaitSingle()

        assertEquals("Your application was created. Please wait for decision", response.message)

        // await for worker - Init risks processing
        wireMockServer.awaitClientRequest("/api/v1/processing/${response.id}")

        // Callback: launch - Receive risks decision
        webTestClient.post()
            .uri("/api/v1/risks/${response.id}/${RisksDecisionStatus.REJECTED}")
            .exchange()
            .expectStatus().isOk
            .returnResult(UniversalResponse::class.java)
            .responseBody.awaitSingle()

        // await - Init payment return
        wireMockServer.awaitClientRequest("/api/v1/payment/return/${response.id}")

        // Callback: launch - Receive payment return status
        webTestClient.delete()
            .uri("/api/v1/payment/${response.id}")
            .exchange()
            .expectStatus().isOk
            .returnResult(UniversalResponse::class.java)
            .responseBody.awaitSingle()

        // Check final insurance status
        await {
            val finalResponse: Insurance = webTestClient.get()
                .uri("/api/v1/insurance/${response.id}")
                .exchange()
                .expectStatus().isOk
                .returnResult(Insurance::class.java)
                .responseBody.awaitSingle()

            assertEquals(response.id, finalResponse.id)
            assertEquals(InsuranceStatus.REJECTED, finalResponse.status)
        }
    }

    @Test
    fun `Reject policy payment declined`() = runBlocking<Unit> {
        wireMockServer.postStub("/api/v1/processing.*")
        wireMockServer.postStub("/api/v1/payment/init.*")
        wireMockServer.postStub("/api/v1/payment/return.*")


        val request = CreateInsuranceRequest(
            userName = "Name Payment Rejected",
            userAge = 30,
            autoBrand = "audi",
        )

        val response: UniversalResponse = webTestClient.post()
            .uri("/api/v1/insurance")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk
            .returnResult(UniversalResponse::class.java)
            .responseBody.awaitSingle()

        assertEquals("Your application was created. Please wait for decision", response.message)

        // await for worker - Init risks processing
        wireMockServer.awaitClientRequest("/api/v1/processing/${response.id}")

        // Callback: launch - Receive risks decision
        webTestClient.post()
            .uri("/api/v1/risks/${response.id}/${RisksDecisionStatus.ACCEPTED}")
            .exchange()
            .expectStatus().isOk
            .returnResult(UniversalResponse::class.java)
            .responseBody.awaitSingle()

        // await - Init payment init
        wireMockServer.awaitClientRequest("/api/v1/payment/init/${response.id}")

        // Callback: launch - Receive payment status
        webTestClient.post()
            .uri("/api/v1/payment/${response.id}/${PaymentStatus.DECLINED}")
            .exchange()
            .expectStatus().isOk
            .returnResult(UniversalResponse::class.java)
            .responseBody.awaitSingle()

        // await - Init payment return
        wireMockServer.awaitClientRequest("/api/v1/payment/return/${response.id}")

        // Callback: launch - Receive payment return status
        webTestClient.delete()
            .uri("/api/v1/payment/${response.id}")
            .exchange()
            .expectStatus().isOk
            .returnResult(UniversalResponse::class.java)
            .responseBody.awaitSingle()

        // Check final insurance status
        await {
            val finalResponse: Insurance = webTestClient.get()
                .uri("/api/v1/insurance/${response.id}")
                .exchange()
                .expectStatus().isOk
                .returnResult(Insurance::class.java)
                .responseBody.awaitSingle()

            assertEquals(response.id, finalResponse.id)
            assertEquals(InsuranceStatus.REJECTED, finalResponse.status)
        }
    }
}