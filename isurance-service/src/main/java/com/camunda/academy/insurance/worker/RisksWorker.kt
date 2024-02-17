package com.camunda.academy.insurance.worker

import com.camunda.academy.insurance.client.RiskServiceClient
import io.camunda.zeebe.spring.client.annotation.JobWorker
import io.camunda.zeebe.spring.client.annotation.Variable
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.runBlocking
import mu.KLogging
import org.springframework.stereotype.Component

@Component
class RisksWorker(
    val riskServiceClient: RiskServiceClient
) {

    @JobWorker(type = "insurance.initRisksProcessing")
    fun initRisksProcessing(@Variable id: String) = runBlocking {
        riskServiceClient.initRisksProcessing(id).awaitSingleOrNull()
    }

    private companion object : KLogging()
}