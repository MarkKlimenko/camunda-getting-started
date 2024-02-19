package com.camunda.academy.payment.controller

import com.camunda.academy.payment.controller.dto.UniversalResponse
import com.camunda.academy.payment.persistence.entity.Payment
import com.camunda.academy.payment.service.PaymentService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/payment")
class PaymentController(
    val paymentService: PaymentService,
) {

    @GetMapping("{id}")
    suspend fun getPayment(
        @PathVariable id: String
    ): Payment = paymentService.getPayment(id)


    @PostMapping("init/{externalId}")
    suspend fun initPayment(
        @PathVariable externalId: String
    ): UniversalResponse = paymentService.initPayment(externalId)

    @PostMapping("process/{id}")
    suspend fun processPayment(
        @PathVariable id: String
    ): Unit = paymentService.processPayment(id)

    @PostMapping("reject/{id}")
    suspend fun rejectPayment(
        @PathVariable id: String
    ): Unit = paymentService.rejectPayment(id)

    @PostMapping("return/{externalId}")
    suspend fun returnFunds(
        @PathVariable externalId: String
    ): Unit = paymentService.returnFunds(externalId)
}