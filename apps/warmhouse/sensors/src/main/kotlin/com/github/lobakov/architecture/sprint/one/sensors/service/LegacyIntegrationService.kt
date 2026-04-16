package com.github.lobakov.architecture.sprint.one.sensors.service

import com.github.lobakov.architecture.sprint.one.sensors.model.dto.LegacyTemperatureResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class LegacyIntegrationService(
    private val legacyTemperatureClient: RestTemplate
) {

    @Value("\${legacy.temperature-api.url}")
    private lateinit var legacyApiUrl: String

    companion object {
        private val LOGGER = LoggerFactory.getLogger(LegacyIntegrationService::class.java)
    }

    fun getTemperatureFromLegacySystem(sensorId: String): LegacyTemperatureResponse? = try {
        legacyTemperatureClient.getForObject(
            "$legacyApiUrl/api/v1/sensors/$sensorId",
            LegacyTemperatureResponse::class.java
        )
    } catch (e: Exception) {
        val message = "Failed to fetch temperature from legacy system for sensor $sensorId: ${e.localizedMessage}"
        LOGGER.error(message)
        throw RuntimeException(message)
    }

    fun updateTemperatureInLegacySystem(sensorId: String, value: Double, status: String): Boolean = try {
        val url = "$legacyApiUrl/api/v1/sensors/$sensorId/value"
        val request = mapOf("value" to value, "status" to status)
        legacyTemperatureClient.patchForObject(url, request, Void::class.java)
        true
    } catch (e: Exception) {
        LOGGER.error("Failed to update temperature in legacy system for sensor $sensorId: ${e.localizedMessage}")
        false
    }

    fun getAllTemperatureSensorsFromLegacy(): List<LegacyTemperatureResponse> = try {
        legacyTemperatureClient.getForObject(
            "$legacyApiUrl/api/v1/sensors",
            Array<LegacyTemperatureResponse>::class.java
        )?.toList() ?: emptyList()
    } catch (e: Exception) {
        LOGGER.error("Failed to fetch sensors from legacy system: ${e.localizedMessage}")
        emptyList()
    }
}
