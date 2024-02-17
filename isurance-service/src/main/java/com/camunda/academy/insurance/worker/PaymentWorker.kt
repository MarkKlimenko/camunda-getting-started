package com.camunda.academy.insurance.worker

import com.camunda.academy.insurance.client.PaymentServiceClient
import io.camunda.zeebe.spring.client.annotation.JobWorker
import io.camunda.zeebe.spring.client.annotation.Variable
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.runBlocking
import mu.KLogging
import org.springframework.stereotype.Component

@Component
class PaymentWorker(
    val paymentServiceClient: PaymentServiceClient
) {

    @JobWorker(type = "insurance.issue.initPaymentProcess")
    fun initPaymentProcess(@Variable id: String) = runBlocking {
        paymentServiceClient.initPaymentProcess(id).awaitSingleOrNull()
    }

    @JobWorker(type = "insurance.reject.initFundsReturning")
    fun initFundsReturning(@Variable id: String) = runBlocking {
        paymentServiceClient.initFundsReturning(id).awaitSingleOrNull()
    }

    private companion object : KLogging()
}