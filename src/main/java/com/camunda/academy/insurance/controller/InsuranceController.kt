package com.camunda.academy.insurance.controller

import com.camunda.academy.insurance.controller.dto.insurance.CreateInsuranceRequest
import com.camunda.academy.insurance.controller.dto.insurance.CreateInsuranceResponse
import com.camunda.academy.insurance.controller.dto.insurance.DecideOnApplicationResponse
import com.camunda.academy.insurance.service.InsuranceService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/insurance")
class InsuranceController(
    val insuranceService: InsuranceService
) {

    @PostMapping
    suspend fun createInsurance(
        @RequestBody request: CreateInsuranceRequest
    ): CreateInsuranceResponse =
        insuranceService.createInsurance(request)

    @PostMapping("decide/{id}/{decision}")
    suspend fun decideOnApplication(
        @PathVariable id: String,
        @PathVariable decision: String
    ): DecideOnApplicationResponse =
        insuranceService.decideOnApplication(id, decision)
}