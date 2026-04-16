package com.github.lobakov.architecture.sprint.one.sensors.model.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
data class LegacyTemperatureResponse(
    val value: Double,
    val unit: String,
    val timestamp: String,
    val location: String,
    val status: String,
    @field:JsonProperty("sensor_id")
    val sensorId: String,
    @field:JsonProperty("sensor_type")
    val sensorType: String,
    val description: String
)
