package com.camunda.academy.insurance.dto

data class InsuranceDto(
    val id: String,
    val status: Status,
    val riskLevel: String? = null,

    val userName: String,
    val userAge: Int,
    val autoBrand: String,
) {
    enum class Status {
        PENDING, SUCCESS, REJECTED
    }
}