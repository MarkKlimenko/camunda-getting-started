package com.camunda.academy.insurance.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("insurances")
data class Insurance(
    @field:Id
    @field:Column("id")
    val id: String? = null,

    @field:Column("status")
    val status: InsuranceStatus? = null,

    @field:Column("user_name")
    val userName: String? = null,

    @field:Column("user_age")
    val userAge: Int? = null,

    @field:Column("auto_brand")
    val autoBrand: String? = null,

    @field:Version
    val version: Int? = null
)