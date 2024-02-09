package com.camunda.academy.insurance.controller

import com.camunda.academy.insurance.controller.dto.insurance.UniversalResponse
import com.camunda.academy.insurance.dto.ApplicationDecision
import com.camunda.academy.insurance.service.risk.RiskService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/insurance")
class InsuranceController(
    val riskService: RiskService,
) {
    @PostMapping("operator/decide/{id}/{decision}")
    suspend fun decideOnApplication(
        @PathVariable id: String,
        @PathVariable decision: ApplicationDecision
    ): UniversalResponse = riskService.decideOnApplication(id, decision)

    @PostMapping("user/info/{id}")
    suspend fun addUserAdditionalInfo(
        @PathVariable id: String
    ): UniversalResponse = riskService.addUserAdditionalInfo(id)
}