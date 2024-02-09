package com.camunda.academy.insurance.controller

import com.camunda.academy.insurance.controller.dto.UniversalResponse
import com.camunda.academy.insurance.service.PaymentService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/insurance")
class PaymentController(
    val paymentService: PaymentService,
) {

    @PostMapping("payment-system/callback/{id}")
    suspend fun callbackPayment(
        @PathVariable id: String
    ): UniversalResponse = paymentService.callbackPayment(id)
}