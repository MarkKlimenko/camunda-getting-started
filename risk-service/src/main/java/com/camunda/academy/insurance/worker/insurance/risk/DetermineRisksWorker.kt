package com.camunda.academy.insurance.worker.insurance.risk

import com.camunda.academy.insurance.dto.RiskLevel
import io.camunda.zeebe.spring.client.annotation.JobWorker
import io.camunda.zeebe.spring.client.annotation.VariablesAsType
import org.springframework.stereotype.Component

@Component
class DetermineRisksWorker() {

    @JobWorker(type = "insurance.risk.determineRisks")
    fun determineRisks(@VariablesAsType request: DetermineRiskRequest): DetermineRiskResponse {
        val riskLevel: RiskLevel = when {
            request.userAge < 20 -> RiskLevel.RED
            request.userAge > 40 -> RiskLevel.YELLOW
            else -> RiskLevel.GREEN
        }

        return DetermineRiskResponse(
            riskLevel = riskLevel
        )
    }

    data class DetermineRiskRequest(
        val id: String,
        val userName: String,
        val userAge: Int,
        val autoBrand: String,
    )

    data class DetermineRiskResponse(
        val riskLevel: RiskLevel
    )
}