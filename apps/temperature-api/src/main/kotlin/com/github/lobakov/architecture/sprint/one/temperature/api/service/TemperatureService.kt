package com.github.lobakov.architecture.sprint.one.temperature.api.service

import com.github.lobakov.architecture.sprint.one.temperature.api.mapper.DtoMapper
import com.github.lobakov.architecture.sprint.one.temperature.api.model.dto.TemperatureResponse
import com.github.lobakov.architecture.sprint.one.temperature.api.repository.SensorRepository
import org.springframework.stereotype.Service
import java.lang.System.currentTimeMillis
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.random.Random

@Service
class TemperatureService(
    private val sensorRepository: SensorRepository,
    private val dtoMapper: DtoMapper
) {

    companion object {
        private const val MIN_TEMPERATURE = -50.0
        private const val MAX_TEMPERATURE = 51.0
    }

    private val random = Random(currentTimeMillis())

    fun getTemperature(location: String): TemperatureResponse {
        val sensor = sensorRepository.findAllByLocation(location).first()
        val value = getRandomTemperature()
        return dtoMapper.sensorDaoToResponse(sensor, value)
    }

    private fun getRandomTemperature(): Float = BigDecimal.valueOf(
        random.nextDouble(MIN_TEMPERATURE, MAX_TEMPERATURE)
    ).setScale(1, RoundingMode.HALF_UP).toFloat()
}
