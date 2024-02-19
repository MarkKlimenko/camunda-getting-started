package com.camunda.academy.payment.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("payments")
data class Payment(
    @field:Id
    @field:Column("id")
    val id: String,

    @field:Column("external_id")
    val externalId: String,

    @field:Column("status")
    val status: PaymentStatus,

    @field:Version
    val version: Int? = null
)