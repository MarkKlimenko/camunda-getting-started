package com.camunda.academy.insurance.worker

import com.camunda.academy.insurance.persistence.entity.Insurance
import com.camunda.academy.insurance.persistence.entity.InsuranceStatus
import com.camunda.academy.insurance.persistence.repository.InsuranceRepository
import io.camunda.zeebe.spring.client.annotation.JobWorker
import io.camunda.zeebe.spring.client.annotation.Variable
import kotlinx.coroutines.runBlocking
import mu.KLogging
import org.springframework.stereotype.Component

@Component
class RejectPolicyWorker(
    val insuranceRepository: InsuranceRepository,
) {

    @JobWorker(type = "insurance.reject.rejectPolicy")
    fun rejectPolicy(@Variable id: String): Map<String, InsuranceStatus> = runBlocking {
        val insurance: Insurance = insuranceRepository.findById(id)
            ?: error("Insurance not found")

        insuranceRepository.save(insurance.copy(status = InsuranceStatus.REJECTED))

        logger.info { "Internal: Policy rejected. id=$id" }
        mapOf("status" to InsuranceStatus.REJECTED)
    }

    @JobWorker(type = "insurance.reject.sendRejection")
    fun sendRejection(@Variable id: String) {
        // TODO: add queue
        logger.info { "User: Rejection sent by email. id=$id" }
    }

    private companion object : KLogging()
}