package com.github.lobakov.architecture.sprint.one.temperature.api.model.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

private const val DEFAULT_TEMPERATURE_UNIT = "°C"

@JsonInclude(NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class TemperatureResponse(
    @field:JsonProperty("sensor_id")
    val sensorId: Long,
    val description: String,
    @field:JsonProperty("sensor_type")
    val sensorType: String,
    val status: String,
    val location: String,
    val value: Float,
    val unit: String = DEFAULT_TEMPERATURE_UNIT,
    val timestamp: LocalDateTime
)
