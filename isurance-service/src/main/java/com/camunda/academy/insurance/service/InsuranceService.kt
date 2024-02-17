package com.camunda.academy.insurance.service

import com.camunda.academy.insurance.controller.dto.CreateInsuranceRequest
import com.camunda.academy.insurance.controller.dto.UniversalResponse
import com.camunda.academy.insurance.persistence.entity.Insurance
import com.camunda.academy.insurance.persistence.entity.InsuranceStatus
import com.camunda.academy.insurance.persistence.repository.InsuranceRepository
import io.camunda.zeebe.client.ZeebeClient
import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.HttpClientErrorException
import java.util.UUID

@Service
class InsuranceService(
    val client: ZeebeClient,
    val insuranceRepository: InsuranceRepository,
) {

    @Transactional(rollbackFor = [Exception::class])
    suspend fun createInsurance(request: CreateInsuranceRequest): UniversalResponse {
        try {
            val insurance = Insurance(
                id = UUID.randomUUID().toString(),
                status = InsuranceStatus.PENDING,
                userName = request.userName,
                userAge = request.userAge,
                autoBrand = request.autoBrand,
            )

            insuranceRepository.save(insurance)

            client.newCreateInstanceCommand()
                .bpmnProcessId("insurance.applyForPolicy")
                .latestVersion()
                .variables(mapOf(
                    "id" to insurance.id,
                    "userName" to insurance.userName,
                    "userAge" to insurance.userAge,
                    "autoBrand" to insurance.autoBrand,
                ))
                .send()
                .join()

            logger.info { "User: application created dto = $insurance" }

            return UniversalResponse(
                id = insurance.id,
                message = "Your application was created. Please wait for decision"
            )
        } catch (e: Exception) {
            logger.error(e) { "Operation failed. Try later" }
            throw HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Operation failed. Try later")
        }
    }

    private companion object : KLogging()
}