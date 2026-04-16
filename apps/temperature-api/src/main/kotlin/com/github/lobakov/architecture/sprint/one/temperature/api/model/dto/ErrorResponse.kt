package com.github.lobakov.architecture.sprint.one.temperature.api.model.dto

data class ErrorResponse(
    val error: String,
    val message: String,
    val path: String? = null
)
