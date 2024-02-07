package com.camunda.academy.insurance.controller

import com.camunda.academy.insurance.controller.dto.insurance.CreateInsuranceRequest
import com.camunda.academy.insurance.controller.dto.insurance.UniversalResponse
import com.camunda.academy.insurance.dto.ApplicationDecision
import com.camunda.academy.insurance.service.InsuranceService
import com.camunda.academy.insurance.service.reject.RejectPolicyService
import com.camunda.academy.insurance.service.risk.RiskService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/insurance")
class InsuranceController(
    val insuranceService: InsuranceService,
    val riskService: RiskService,
    val rejectPolicyService: RejectPolicyService,
) {

    @PostMapping
    suspend fun createInsurance(
        @RequestBody request: CreateInsuranceRequest
    ): UniversalResponse = insuranceService.createInsurance(request)

    @PostMapping("decide/{id}/{decision}")
    suspend fun decideOnApplication(
        @PathVariable id: String,
        @PathVariable decision: ApplicationDecision
    ): UniversalResponse = riskService.decideOnApplication(id, decision)

    @PostMapping("info/{id}")
    suspend fun addUserAdditionalInfo(
        @PathVariable id: String
    ): UniversalResponse = riskService.addUserAdditionalInfo(id)

    @PostMapping("return-funds/{id}")
    suspend fun operatorReturnFunds(
        @PathVariable id: String
    ): UniversalResponse = rejectPolicyService.operatorReturnFunds(id)
}