package com.camunda.academy.insurance

import com.camunda.academy.insurance.controller.dto.CreateInsuranceRequest
import com.camunda.academy.insurance.controller.dto.UniversalResponse
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
    fun commonFlowTest() = runBlocking<Unit> {
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

        // await for worker
    }
}