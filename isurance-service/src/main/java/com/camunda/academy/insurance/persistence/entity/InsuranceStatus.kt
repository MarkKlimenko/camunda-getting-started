package com.camunda.academy.insurance.persistence.entity

enum class InsuranceStatus {
    PENDING,

    /**
     * Insurance is on manual stage of issuing
     * Due to some problems during the issuing
     * Operator has to act on this action status
     */
    PENDING_MANUAL,
    SUCCESS,
    REJECTED
}
