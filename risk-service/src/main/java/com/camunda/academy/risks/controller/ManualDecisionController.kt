package com.camunda.academy.risks.controller

import com.camunda.academy.risks.controller.dto.insurance.UniversalResponse
import com.camunda.academy.risks.dto.ManualDecision
import com.camunda.academy.risks.service.RiskService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/risks")
class ManualDecisionController(
    val riskService: RiskService,
) {

    @PostMapping("manual/operator/determine/{id}/{decision}")
    suspend fun makeOperatorDecision(
        @PathVariable id: String,
        @PathVariable decision: ManualDecision
    ): UniversalResponse = riskService.decideOnApplication(id, decision)

    // TODO: Add multipart with request logger
    @PostMapping("manual/user/additional-info/{id}")
    suspend fun addUserAdditionalInfo(
        @PathVariable id: String
    ): UniversalResponse = riskService.addUserAdditionalInfo(id)
}
