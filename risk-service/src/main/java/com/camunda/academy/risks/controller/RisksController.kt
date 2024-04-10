package com.camunda.academy.risks.controller

import com.camunda.academy.risks.controller.dto.insurance.UniversalResponse
import com.camunda.academy.risks.service.RiskService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/risks")
class RisksController(
    val riskService: RiskService,
) {
    @PostMapping("determine")
    suspend fun determineRisks(
        @RequestBody parameters: ApplicationParameters
    ): UniversalResponse = riskService.determineRisks(parameters)
}
