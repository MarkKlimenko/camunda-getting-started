package com.camunda.academy.payment.client

import com.camunda.academy.payment.persistence.entity.PaymentStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import reactivefeign.spring.config.ReactiveFeignClient
import reactor.core.publisher.Mono

@ReactiveFeignClient(
    name = "insurance-service",
    url = "\${client.insurance-service.url}"
)
interface InsuranceServiceClient {

    @PostMapping("api/v1/payment/{externalId}/{paymentStatus}")
    fun sendPaymentCallback(
        @PathVariable externalId: String,
        @PathVariable paymentStatus: PaymentStatus
    ): Mono<Void>
}