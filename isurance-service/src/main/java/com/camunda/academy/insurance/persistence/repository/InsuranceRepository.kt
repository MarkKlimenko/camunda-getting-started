package com.camunda.academy.insurance.persistence.repository

import com.camunda.academy.insurance.persistence.entity.Insurance
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface InsuranceRepository : CoroutineCrudRepository<Insurance, String> {
}