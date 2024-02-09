package com.camunda.academy.insurance.controller.dto

data class CreateInsuranceRequest(
    val userName: String,
    val userAge: Int,
    val autoBrand: String
)
