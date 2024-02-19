package com.camunda.academy.insurance.util

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import org.springframework.http.HttpStatus

fun WireMockServer.postStub(urlRegex: String) {
    this.stubFor(
        WireMock.post(WireMock.urlMatching(urlRegex))
            .willReturn(
                WireMock.aResponse()
                    .withStatus(HttpStatus.OK.value())
            )
    )
}

fun WireMockServer.awaitClientRequest(urlRegex: String) {
    await {
        this.verify(
            WireMock.postRequestedFor(WireMock.urlMatching(urlRegex))
        )
    }
}