package com.camunda.academy.insurance.controller.dto.insurance

data class CreateInsuranceRequest(
    val userName: String,
    val userAge: Int,
    val autoBrand: String
)
