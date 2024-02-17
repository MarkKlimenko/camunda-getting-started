package com.camunda.academy.insurance

import com.camunda.academy.insurance.controller.dto.CreateInsuranceRequest
import com.camunda.academy.insurance.controller.dto.PaymentStatus
import com.camunda.academy.insurance.controller.dto.RisksDecisionStatus
import com.camunda.academy.insurance.controller.dto.UniversalResponse
import com.camunda.academy.insurance.persistence.entity.Insurance
import com.camunda.academy.insurance.persistence.entity.InsuranceStatus
import com.github.tomakehurst.wiremock.client.WireMock
import io.camunda.zeebe.client.ZeebeClient
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.runBlocking
import org.awaitility.Awaitility
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.opentest4j.AssertionFailedError
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import java.time.Duration
import java.util.concurrent.TimeUnit

class ApplyForPolicyTest : WebIntegrationTest() {

    @Autowired
    lateinit var client: ZeebeClient

    @Test
    fun commonFlowTest() = runBlocking<Unit> {
        wireMockServer.stubFor(
            WireMock.post(WireMock.urlMatching("/api/v1/processing.*"))
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                )
        )

        wireMockServer.stubFor(
            WireMock.post(WireMock.urlMatching("/api/v1/payment/init.*"))
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                )
        )


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
        Awaitility.waitAtMost(Duration.ofSeconds(20))
            .pollInterval(500, TimeUnit.MILLISECONDS)
            .until {
                try {
                    WireMock.verify(
                        WireMock.postRequestedFor(WireMock.urlMatching("/api/v1/processing/${response.id}"))
                    )

                    true
                } catch (e: AssertionFailedError) {
                    println(e)
                    false
                }
            }

        // Callback: launch - Receive risks decision
        webTestClient.post()
            .uri("/api/v1/risks/${response.id}/${RisksDecisionStatus.ACCEPTED}")
            .exchange()
            .expectStatus().isOk
            .returnResult(UniversalResponse::class.java)
            .responseBody.awaitSingle()

        // await - Init payment process
        Awaitility.waitAtMost(Duration.ofSeconds(20))
            .pollInterval(500, TimeUnit.MILLISECONDS)
            .until {
                try {
                    WireMock.verify(
                        WireMock.postRequestedFor(WireMock.urlMatching("/api/v1/payment/init/${response.id}"))
                    )

                    true
                } catch (e: AssertionFailedError) {
                    println(e)
                    false
                }
            }

        // Callback: launch - Receive payment status
        webTestClient.post()
            .uri("/api/v1/payment/${response.id}/${PaymentStatus.RECEIVED}")
            .exchange()
            .expectStatus().isOk
            .returnResult(UniversalResponse::class.java)
            .responseBody.awaitSingle()


        // TODO: await worker "User: send policy" to process - rm awaitility for the next step

        // Check final insurance status
        Awaitility.waitAtMost(Duration.ofSeconds(20))
            .pollInterval(500, TimeUnit.MILLISECONDS)
            .until {
                try {
                    runBlocking {
                        val finalResponse: Insurance = webTestClient.get()
                            .uri("/api/v1/insurance/${response.id}")
                            .exchange()
                            .expectStatus().isOk
                            .returnResult(Insurance::class.java)
                            .responseBody.awaitSingle()

                        assertEquals(response.id, finalResponse.id)
                        assertEquals(InsuranceStatus.SUCCESS, finalResponse.status)
                    }
                    true
                } catch (e: AssertionFailedError) {
                    println(e)
                    false
                }
            }
    }
}