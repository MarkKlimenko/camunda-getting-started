package com.camunda.academy.insurance

import io.camunda.zeebe.client.ZeebeClient
import io.camunda.zeebe.client.api.response.ActivatedJob
import io.cucumber.java.Before
import io.cucumber.java.BeforeStep
import io.cucumber.java.Scenario
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import org.springframework.beans.factory.annotation.Autowired

class StepDefs : BpmnTest() {
    @Autowired
    lateinit var client: ZeebeClient

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
    fun startProcess(processId: String, parameters: Map<String, Any>) {
        println("Process '$processId' started with parameters: $parameters")

        client.newCreateInstanceCommand()
            .bpmnProcessId(processId)
            .latestVersion()
            .variables(parameters)
            .send()
            .join()
    }

    @Then("Receive task {string}")
    fun receiveTaskWithParameters(taskId: String, parameters: Map<String, Any>?) {
        println("Expect task '$taskId' and send parameters: $parameters")

        val job: ActivatedJob = client.newActivateJobsCommand()
            .jobType(taskId)
            .maxJobsToActivate(1)
            .send()
            .join()
            .jobs
            .first()

        client.newCompleteCommand(job.getKey()).variables(parameters).send().join()
    }

    @Then("Send task {string} with key {string}")
    fun sendTask(taskId: String, correlationKey: String, parameters: Map<String, Any>) {
        println("Send task '$taskId' and send parameters: $parameters")

        client.newPublishMessageCommand()
            .messageName(taskId)
            .correlationKey(correlationKey)
            .variables(parameters)
            .send()
    }
}