package com.camunda.academy.risks.worker

import com.camunda.academy.risks.dto.AutomaticDecision
import io.camunda.zeebe.spring.client.annotation.JobWorker
import io.camunda.zeebe.spring.client.annotation.Variable
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component

@Component
class AutomaticDecisionWorker {

    @JobWorker(type = "risk.determine.makeAutomaticDecision")
    fun makeAutomaticDecision(@Variable id: String) : Map<String, AutomaticDecision> = runBlocking {
        throw NotImplementedError()
    }
}
