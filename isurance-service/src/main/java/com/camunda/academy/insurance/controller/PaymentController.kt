package com.camunda.academy.insurance.controller

import com.camunda.academy.insurance.controller.dto.PaymentStatus
import com.camunda.academy.insurance.controller.dto.UniversalResponse
import com.camunda.academy.insurance.service.PaymentService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/payment")
class PaymentController(
    val paymentService: PaymentService
) {

    @PostMapping("{id}/{paymentStatus}")
    suspend fun receivePayment(
        @PathVariable id: String,
        @PathVariable paymentStatus: PaymentStatus
    ): UniversalResponse = paymentService.receivePayment(id, paymentStatus)

    @DeleteMapping("{id}")
    suspend fun receiveFundsReturning(
        @PathVariable id: String
    ): UniversalResponse = paymentService.receiveFundsReturning(id)
}