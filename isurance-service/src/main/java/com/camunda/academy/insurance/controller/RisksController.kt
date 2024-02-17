package com.camunda.academy.insurance.controller

import com.camunda.academy.insurance.controller.dto.RisksDecisionStatus
import com.camunda.academy.insurance.controller.dto.UniversalResponse
import com.camunda.academy.insurance.service.RisksService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/risks")
class RisksController(
    val risksService: RisksService
) {

    @PostMapping("{id}/{risksDecision}")
    suspend fun processRisksDecision(
        @PathVariable id: String,
        @PathVariable risksDecision: RisksDecisionStatus
    ): UniversalResponse = risksService.processRisksDecision(id, risksDecision)
}