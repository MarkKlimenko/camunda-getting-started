package com.camunda.academy.insurance

import com.camunda.academy.insurance.worker.IssuePolicyWorker
import com.github.tomakehurst.wiremock.WireMockServer
import io.camunda.zeebe.spring.test.ZeebeSpringTest
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 0)
@AutoConfigureWebTestClient
@ZeebeSpringTest
class WebIntegrationTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired(required = false)
    lateinit var wireMockServer: WireMockServer

    @SpyBean
    lateinit var issuePolicyWorker: IssuePolicyWorker

    @BeforeEach
    fun init() {
        wireMockServer.resetAll()
    }
}
