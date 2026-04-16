package com.github.lobakov.architecture.sprint.one.sensors.model.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import java.util.UUID

@JsonInclude(NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
data class SensorEvent(
    @field:JsonProperty("event_id")
    val eventId: String?,
    @field:JsonProperty("event_type")
    val eventType: String?,
    @field:JsonProperty("sensor_id")
    val sensorId: UUID?,
    @field:JsonProperty("home_id")
    val homeId: UUID?,
    @field:JsonProperty("sensor_type")
    val sensorType: String?,
    val data: Map<String, Any> = mapOf(),
    val timestamp: LocalDateTime?
)
