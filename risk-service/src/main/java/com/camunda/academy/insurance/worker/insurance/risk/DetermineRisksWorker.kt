package com.camunda.academy.insurance.worker.insurance.risk

import com.camunda.academy.insurance.dto.InsuranceDto
import io.camunda.zeebe.client.api.response.ActivatedJob
import io.camunda.zeebe.spring.client.annotation.JobWorker
import io.camunda.zeebe.spring.client.annotation.VariablesAsType
import org.springframework.stereotype.Component

@Component
class DetermineRisksWorker() {

    @JobWorker(type = "insurance.risk.determineRisks")
    fun determineRisks(@VariablesAsType dto: InsuranceDto): InsuranceDto {
        val riskLevel: String = when {
            dto.userAge < 20 -> "red"
            dto.userAge > 40 -> "yellow"
            else -> "green"
        }

        //val resultDto: InsuranceDto = dto.copy(riskLevel = riskLevel)
        //insuranceService.save(resultDto)

        return dto.copy(riskLevel = riskLevel)
    }
}