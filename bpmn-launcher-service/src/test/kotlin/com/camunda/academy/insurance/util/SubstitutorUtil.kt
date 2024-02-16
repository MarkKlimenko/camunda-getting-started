package com.camunda.academy.insurance.util

import io.cucumber.datatable.DataTable
import java.util.UUID

fun getCorrelationKey(correlationKey: String, storage: MutableMap<String, String>): String =
    if (correlationKey.contains("uuid()")) {
        UUID.randomUUID().toString()
    } else if (correlationKey.contains("{")) {
        storage[correlationKey.replace("""\W""".toRegex(), "")] ?: "not found in storage"
    } else {
        correlationKey
    }

fun getOutgoingParameters(parameters: Map<String, String>, storage: MutableMap<String, String>): Map<String, Any> =
    parameters.entries
        .map {
            val value = if (it.value.contains("uuid()")) {
                UUID.randomUUID().toString()
            } else if (it.value.contains("{")) {
                storage[it.value.replace("""\W""".toRegex(), "")] ?: "not found in storage"
            } else {
                it.value
            }

            storage[it.key] = value
            it.key to value
        }
        .associate { it.first to it.second }

fun getExpectedIncomingParameters(parameters: DataTable, storage: MutableMap<String, String>): Map<String, Any> =
    getParameters("<-", parameters, storage)

fun getOutgoingParameters(parameters: DataTable, storage: MutableMap<String, String>): Map<String, Any> =
    getParameters("->", parameters, storage)

private fun getParameters(filter: String, parameters: DataTable, storage: MutableMap<String, String>): Map<String, Any> =
    parameters.asLists<String>(String::class.java)
        .filter { it[0] == filter }
        .map {
            val value: String = if (it[2].contains("{")) {
                storage[it[2].replace("""\W""".toRegex(), "")] ?: "not found in storage"
            } else {
                it[2]
            }

            storage[it[1]] = value
            it[1] to value
        }
        .associate { it.first to it.second }