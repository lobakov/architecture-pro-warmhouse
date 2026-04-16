package com.github.lobakov.architecture.sprint.one.temperature.api.mapper

import com.github.lobakov.architecture.sprint.one.temperature.api.model.dao.Sensor
import com.github.lobakov.architecture.sprint.one.temperature.api.model.dto.TemperatureResponse
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class DtoMapper {

    fun sensorDaoToResponse(sensor: Sensor, value: Float) = TemperatureResponse(
        sensorId = sensor.id,
        description = sensor.name,
        sensorType = sensor.type,
        status = sensor.status,
        location = sensor.location,
        value = value,
        unit = sensor.unit,
        timestamp = LocalDateTime.now()
    )
}
