package com.camunda.academy.insurance.worker

import com.camunda.academy.insurance.dto.InsuranceDto
import com.camunda.academy.insurance.service.InsuranceService
import io.camunda.zeebe.spring.client.annotation.JobWorker
import io.camunda.zeebe.spring.client.annotation.VariablesAsType
import org.springframework.stereotype.Component

@Component
class IssuePolicyWorker(
    val insuranceService: InsuranceService
) {

    @JobWorker(type = "insurance.issuePolicy")
    fun issuePolicy(@VariablesAsType dto: InsuranceDto) {

    }
}