package com.camunda.academy.risks.worker

import io.camunda.zeebe.spring.client.annotation.JobWorker
import io.camunda.zeebe.spring.client.annotation.Variable
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component

@Component
class ManualDecisionWorker {

    @JobWorker(type = "risk.determine.manual.sendOperatorAlert")
    fun sendOperatorAlert(@Variable id: String) = runBlocking<Unit> {
        throw NotImplementedError()
    }

    @JobWorker(type = "risk.determine.manual.sendOperatorReminder")
    fun sendOperatorReminder(@Variable id: String) = runBlocking<Unit> {
        throw NotImplementedError()
    }

    @JobWorker(type = "risk.determine.manual.sendManagerReminder")
    fun sendManagerReminder(@Variable id: String) = runBlocking<Unit> {
        throw NotImplementedError()
    }

    @JobWorker(type = "risk.determine.manual.sendUserAdditionalInfoRequest")
    fun sendUserAdditionalInfoRequest(@Variable id: String) = runBlocking<Unit> {
        throw NotImplementedError()
    }

    // TODO: how to test timeouts?
    @JobWorker(type = "risk.determine.manual.sendUserAdditionalInfoReminder")
    fun sendUserAdditionalInfoReminder(@Variable id: String) = runBlocking<Unit> {
        throw NotImplementedError()
    }

    @JobWorker(type = "risk.determine.manual.rejectByUserTimeout")
    fun rejectByUserTimeout(@Variable id: String) = runBlocking<Unit> {
        throw NotImplementedError()
    }
}
