package com.camunda.academy.insurance

import com.camunda.academy.insurance.util.getCorrelationKey
import com.camunda.academy.insurance.util.getExpectedIncomingParameters
import com.camunda.academy.insurance.util.getOutgoingParameters
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.camunda.zeebe.client.ZeebeClient
import io.camunda.zeebe.client.api.response.ActivatedJob
import io.cucumber.datatable.DataTable
import io.cucumber.java.Before
import io.cucumber.java.BeforeStep
import io.cucumber.java.Scenario
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired

class StepDefs : BpmnTest() {
    @Autowired
    lateinit var client: ZeebeClient

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Before
    fun setup(scenario: Scenario) {
        println("## Scenario: ${scenario.name} Running on: ${Thread.currentThread().name}")
        //TODO: stop all processes
    }

    @BeforeStep
    fun step(scenario: Scenario) {
        println("## Scenario: ${scenario.name} Steps running on: ${Thread.currentThread().name}")
    }

    @Given("Start process {string}")
    fun startProcess(processId: String, parameters: Map<String, String>) {
        val outgoingParameters: Map<String, Any> = getOutgoingParameters(parameters, storage)

        println("Process '$processId' started with parameters: $outgoingParameters")

        client.newCreateInstanceCommand()
            .bpmnProcessId(processId)
            .latestVersion()
            .variables(outgoingParameters)
            .send()
            .join()
    }

    @Then("Receive task {string}")
    fun receiveTaskWithParameters(taskId: String, parameters: DataTable) {
        val expectedIncomingParameters: Map<String, Any> = getExpectedIncomingParameters(parameters, storage)
        val outgoingParameters: Map<String, Any> = getOutgoingParameters(parameters, storage)

        println("Expect task '$taskId' with expectedIncomingParams = $expectedIncomingParameters and outgoingParams = $outgoingParameters")

        val job: ActivatedJob = client.newActivateJobsCommand()
            .jobType(taskId)
            .maxJobsToActivate(1)
            .send()
            .join()
            .jobs
            .first()

        val variables: Map<String, String> = objectMapper.readValue(job.variables)
        expectedIncomingParameters.forEach {
            assertEquals(it.value, variables[it.key] ?: "variable not found")
        }

        client.newCompleteCommand(job.getKey()).variables(outgoingParameters).send().join()
    }

    @Then("Receive task {string} and fail with error {string}")
    fun receiveTaskWithError(taskId: String, errorType: String, parameters: DataTable) {
        val expectedIncomingParameters: Map<String, Any> = getExpectedIncomingParameters(parameters, storage)

        println("Expect task '$taskId' with expectedIncomingParams = $expectedIncomingParameters and fail")

        val job: ActivatedJob = client.newActivateJobsCommand()
            .jobType(taskId)
            .maxJobsToActivate(1)
            .send()
            .join()
            .jobs
            .first()

        val variables: Map<String, String> = objectMapper.readValue(job.variables)
        expectedIncomingParameters.forEach {
            assertEquals(it.value, variables[it.key] ?: "variable not found")
        }

        client.newThrowErrorCommand(job.getKey()).errorCode(errorType).send().join()
    }

    @Then("Send task {string} with key {string}")
    fun sendTask(taskId: String, correlationKey: String, parameters: Map<String, String>) {
        val processedCorrelationKey: String = getCorrelationKey(correlationKey, storage)
        val outgoingParameters: Map<String, Any> = getOutgoingParameters(parameters, storage)

        println("Send task '$taskId' and send parameters: $outgoingParameters")

        client.newPublishMessageCommand()
            .messageName(taskId)
            .correlationKey(processedCorrelationKey)
            .variables(outgoingParameters)
            .send()
    }
}