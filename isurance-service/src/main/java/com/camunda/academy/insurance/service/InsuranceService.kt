package com.camunda.academy.insurance.service

import com.camunda.academy.insurance.controller.dto.insurance.CreateInsuranceRequest
import com.camunda.academy.insurance.controller.dto.insurance.UniversalResponse
import com.camunda.academy.insurance.dto.InsuranceDto
import io.camunda.zeebe.client.ZeebeClient
import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.client.HttpClientErrorException
import java.util.UUID

@Service
class InsuranceService(
    val client: ZeebeClient
) {
    private val storage: MutableMap<String, InsuranceDto> = mutableMapOf()

    suspend fun createInsurance(
        @RequestBody request: CreateInsuranceRequest
    ): UniversalResponse {
        try {
            val insuranceDto = InsuranceDto(
                id = UUID.randomUUID().toString(),
                status = InsuranceDto.Status.PENDING,
                userName = request.userName,
                userAge = request.userAge,
                autoBrand = request.autoBrand,
            )

            storage[insuranceDto.id] = insuranceDto

            client.newCreateInstanceCommand()
                .bpmnProcessId("insuranceProcess")
                .latestVersion()
                .variables(insuranceDto)
                .send()
                .join()

            logger.info { "User: application created dto = $insuranceDto" }

            return UniversalResponse(
                id = insuranceDto.id,
                message = "Your application was created. Please wait for decision"
            )
        } catch (e: Exception) {
            logger.error(e) { "Operation failed. Try later" }
            throw HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Operation failed. Try later")
        }
    }

    fun save(dto: InsuranceDto) {
        storage[dto.id] = dto
    }

    private companion object : KLogging()
}