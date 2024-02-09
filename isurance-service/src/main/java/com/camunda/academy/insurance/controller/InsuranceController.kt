package com.camunda.academy.insurance.controller

import com.camunda.academy.insurance.controller.dto.insurance.CreateInsuranceRequest
import com.camunda.academy.insurance.controller.dto.insurance.UniversalResponse
import com.camunda.academy.insurance.dto.InsuranceDto
import com.camunda.academy.insurance.service.InsuranceService
import com.camunda.academy.insurance.service.IssuePolicyService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/insurance")
class InsuranceController(
    val insuranceService: InsuranceService,
    val issuePolicyService: IssuePolicyService,
) {

    @PostMapping("user")
    suspend fun createInsurance(
        @RequestBody request: CreateInsuranceRequest
    ): UniversalResponse = insuranceService.createInsurance(request)

    @PostMapping("operator/issue-policy/retry/{id}/{status}")
    suspend fun retryIssuePolicy(
        @PathVariable id: String,
        @PathVariable status: InsuranceDto.Status
    ): UniversalResponse = issuePolicyService.retryIssuePolicy(id, status)
}