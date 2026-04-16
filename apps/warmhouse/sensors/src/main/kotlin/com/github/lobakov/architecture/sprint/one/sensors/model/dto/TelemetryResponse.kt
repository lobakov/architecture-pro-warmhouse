package com.github.lobakov.architecture.sprint.one.sensors.model.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY

@JsonInclude(NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
data class TelemetryResponse(
    val name: String?,
    val value: String?,
    val timestamp: String?
)
