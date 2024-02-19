package com.camunda.academy.payment.persistence.repository

import com.camunda.academy.payment.persistence.entity.Payment
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface PaymentRepository : CoroutineCrudRepository<Payment, String> {
}