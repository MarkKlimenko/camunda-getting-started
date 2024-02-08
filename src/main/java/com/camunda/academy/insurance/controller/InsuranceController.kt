package com.camunda.academy.insurance.controller

import com.camunda.academy.insurance.controller.dto.insurance.CreateInsuranceRequest
import com.camunda.academy.insurance.controller.dto.insurance.UniversalResponse
import com.camunda.academy.insurance.dto.ApplicationDecision
import com.camunda.academy.insurance.dto.InsuranceDto
import com.camunda.academy.insurance.service.InsuranceService
import com.camunda.academy.insurance.service.issue.IssuePolicyService
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
    val issuePolicyService: IssuePolicyService,
) {

    @PostMapping("user")
    suspend fun createInsurance(
        @RequestBody request: CreateInsuranceRequest
    ): UniversalResponse = insuranceService.createInsurance(request)


    @PostMapping("operator/decide/{id}/{decision}")
    suspend fun decideOnApplication(
        @PathVariable id: String,
        @PathVariable decision: ApplicationDecision
    ): UniversalResponse = riskService.decideOnApplication(id, decision)

    @PostMapping("user/info/{id}")
    suspend fun addUserAdditionalInfo(
        @PathVariable id: String
    ): UniversalResponse = riskService.addUserAdditionalInfo(id)


    @PostMapping("operator/return-funds/{id}")
    suspend fun returnFunds(
        @PathVariable id: String
    ): UniversalResponse = rejectPolicyService.returnFunds(id)


    @PostMapping("payment-system/callback/{id}")
    suspend fun callbackPayment(
        @PathVariable id: String
    ): UniversalResponse = issuePolicyService.callbackPayment(id)

    @PostMapping("operator/issue-policy/retry/{id}/{status}")
    suspend fun retryIssuePolicy(
        @PathVariable id: String,
        @PathVariable status: InsuranceDto.Status
    ): UniversalResponse = issuePolicyService.retryIssuePolicy(id, status)
}