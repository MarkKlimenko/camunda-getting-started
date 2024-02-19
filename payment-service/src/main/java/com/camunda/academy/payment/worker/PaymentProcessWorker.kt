package com.camunda.academy.payment.worker

import com.camunda.academy.payment.client.InsuranceServiceClient
import com.camunda.academy.payment.persistence.entity.PaymentStatus
import com.camunda.academy.payment.persistence.repository.PaymentRepository
import io.camunda.zeebe.spring.client.annotation.JobWorker
import io.camunda.zeebe.spring.client.annotation.Variable
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.runBlocking
import mu.KLogging
import org.springframework.stereotype.Component

@Component
class PaymentProcessWorker(
    val paymentRepository: PaymentRepository,
    val insuranceServiceClient: InsuranceServiceClient,
) {

    @JobWorker(type = "payment.sendInitiatePayment")
    fun sendInitiatePayment(@Variable id: String) {
        logger.info { "User: You have to pay for... . id=$id" }
    }

    @JobWorker(type = "payment.paymentReminder")
    fun paymentReminder(@Variable id: String) {
        logger.info { "User: Do not forget to pay for... . id=$id" }
    }

    @JobWorker(type = "payment.rejectPaymentByTimeout")
    fun rejectPaymentByTimeout(@Variable id: String) = runBlocking {
        val payment = paymentRepository.findById(id)
            ?: error("payment not found")

        paymentRepository.save(
            payment.copy(status = PaymentStatus.DECLINED)
        )

        logger.info { "System: Payment rejected... . id=$id" }
    }

    @JobWorker(type = "payment.sendCallback")
    suspend fun sendCallback(@Variable id: String) = runBlocking {
        val payment = paymentRepository.findById(id)
            ?: error("payment not found")

        insuranceServiceClient.sendPaymentCallback(payment.externalId, payment.status)
            .awaitSingleOrNull()
    }

    private companion object : KLogging()
}