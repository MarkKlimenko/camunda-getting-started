package com.camunda.academy.insurance.client

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import reactivefeign.spring.config.ReactiveFeignClient
import reactor.core.publisher.Mono

@ReactiveFeignClient(
    name = "risk-service",
    url = "\${client.risk-service.url}"
)
interface RiskServiceClient {

    @PostMapping("api/v1/processing/{id}")
    fun initRisksProcessing(
        @PathVariable id: String
    ): Mono<Void>
}