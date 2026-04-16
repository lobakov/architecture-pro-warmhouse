package com.github.lobakov.architecture.sprint.one.sensors.model.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY
import java.util.UUID

@JsonInclude(NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
data class SensorResponse(
    val id: UUID?,
    val name: String?,
    val type: String?,
    val status: String?,
    val indicators: List<IndicatorResponse>
)
