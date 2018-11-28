package com.andrewgiang.assistantsdk.response

data class Service(
    val domain: String,
    val services: Map<String, ServiceInfo>
)

data class ServiceInfo(val description: String, val fields: Map<String, FieldInfo>)

data class FieldInfo(val description: String, val example: Any)