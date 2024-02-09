package com.camunda.academy.insurance.worker

import io.camunda.zeebe.spring.client.annotation.JobWorker
import io.camunda.zeebe.spring.client.annotation.Variable
import io.camunda.zeebe.spring.client.annotation.VariablesAsType
import mu.KLogging
import org.springframework.stereotype.Component

@Component
class ReturnFundsWorker() {

    @JobWorker(type = "insurance.reject.returnFunds")
    fun returnFunds(@Variable id: String) {
        /*if (dto.userAge == 11) {
            throw ZeebeBpmnError("returnFundsError", "Can not process this user age")
        }*/

        // TODO: check - if payment was processed
        logger.info { "Internal: Funds are returned. id=$id" }
    }

    @JobWorker(type = "insurance.reject.operatorAlertReturnFunds")
    fun operatorAlertReturnFunds(@Variable id: String) {
        logger.info { "Operator: Problem during returning funds. id=$id" }
    }

    private companion object : KLogging()
}