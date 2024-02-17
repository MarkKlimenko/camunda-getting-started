package com.camunda.academy.insurance.client

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import reactivefeign.spring.config.ReactiveFeignClient
import reactor.core.publisher.Mono

@ReactiveFeignClient(
    name = "payment-service",
    url = "\${client.payment-service.url}"
)
interface PaymentServiceClient {

    @PostMapping("api/v1/payment/init/{id}")
    fun initPaymentProcess(
        @PathVariable id: String
    ): Mono<Void>

    @PostMapping("api/v1/payment/return/{id}")
    fun initFundsReturning(
        @PathVariable id: String
    ): Mono<Void>
}