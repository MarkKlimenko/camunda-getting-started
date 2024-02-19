package com.camunda.academy.payment.service

import com.camunda.academy.payment.controller.dto.UniversalResponse
import com.camunda.academy.payment.persistence.entity.Payment
import com.camunda.academy.payment.persistence.entity.PaymentStatus
import com.camunda.academy.payment.persistence.repository.PaymentRepository
import io.camunda.zeebe.client.ZeebeClient
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent
import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import java.util.UUID

@Service
class PaymentService(
    val client: ZeebeClient,
    val paymentRepository: PaymentRepository,
) {

    suspend fun getPayment(id: String): Payment =
        paymentRepository.findById(id) ?: error("payment not found")

    suspend fun initPayment(externalId: String): UniversalResponse {
        try {
            val payment = Payment(
                id = UUID.randomUUID().toString(),
                externalId = externalId,
                status = PaymentStatus.PENDING,
            )

            paymentRepository.save(payment)

            val process: ProcessInstanceEvent = client.newCreateInstanceCommand()
                .bpmnProcessId("payment.process")
                .latestVersion()
                .variables(mapOf(
                    "id" to payment.id
                ))
                .send()
                .join()

            logger.info { "User: payment initiated id=${payment.id}, externalId=$externalId, processId=${process.bpmnProcessId}" }

            return UniversalResponse(
                id = payment.id,
                message = "payment initiated"
            )

        } catch (e: Exception) {
            logger.error(e) { "Operation failed. Try later" }
            throw HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Operation failed. Try later")
        }
    }

    suspend fun processPayment(id: String) {
        val payment = paymentRepository.findById(id)
            ?: error("payment not found")

        paymentRepository.save(
            payment.copy(status = PaymentStatus.RECEIVED)
        )

        client.newPublishMessageCommand()
            .messageName("payment.paymentProcessed")
            .correlationKey(id)
            .send()
    }

    suspend fun rejectPayment(id: String) {
        val payment = paymentRepository.findById(id)
            ?: error("payment not found")

        paymentRepository.save(
            payment.copy(status = PaymentStatus.DECLINED)
        )

        client.newPublishMessageCommand()
            .messageName("insurance.reject.operatorReturnFunds")
            .correlationKey(id)
            .send()
    }

    fun returnFunds(id: String) {
        client.newPublishMessageCommand()
            .messageName("insurance.reject.operatorReturnFunds")
            .correlationKey(id)
            .send()
    }

    private companion object : KLogging()
}