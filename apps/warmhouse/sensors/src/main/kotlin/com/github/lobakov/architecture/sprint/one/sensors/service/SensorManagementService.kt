package com.github.lobakov.architecture.sprint.one.sensors.service

import com.github.lobakov.architecture.sprint.one.sensors.integration.SensorEventProducer
import com.github.lobakov.architecture.sprint.one.sensors.model.dao.Sensor
import com.github.lobakov.architecture.sprint.one.sensors.model.dao.SensorIndicator
import com.github.lobakov.architecture.sprint.one.sensors.model.dao.SensorTelemetry
import com.github.lobakov.architecture.sprint.one.sensors.model.dto.IndicatorResponse
import com.github.lobakov.architecture.sprint.one.sensors.model.dto.SensorCommandRequest
import com.github.lobakov.architecture.sprint.one.sensors.model.dto.SensorRequest
import com.github.lobakov.architecture.sprint.one.sensors.model.dto.SensorResponse
import com.github.lobakov.architecture.sprint.one.sensors.model.dto.TelemetryResponse
import com.github.lobakov.architecture.sprint.one.sensors.repository.SensorIndicatorRepository
import com.github.lobakov.architecture.sprint.one.sensors.repository.SensorRepository
import com.github.lobakov.architecture.sprint.one.sensors.repository.SensorStatusRepository
import com.github.lobakov.architecture.sprint.one.sensors.repository.SensorTelemetryRepository
import com.github.lobakov.architecture.sprint.one.sensors.repository.SensorTypeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class SensorManagementService(
    private val sensorRepository: SensorRepository,
    private val sensorTypeRepository: SensorTypeRepository,
    private val sensorStatusRepository: SensorStatusRepository,
    private val sensorIndicatorRepository: SensorIndicatorRepository,
    private val sensorTelemetryRepository: SensorTelemetryRepository,
    private val legacyIntegrationService: LegacyIntegrationService,
    private val eventProducer: SensorEventProducer
) {

    fun createSensor(homeId: UUID, request: SensorRequest): SensorResponse {
        val sensorType = sensorTypeRepository.findByType(request.type)
            ?: throw IllegalArgumentException("Unknown sensor type: ${request.type}")

        val initialStatus = sensorStatusRepository.findByStatus("off")
            ?: throw IllegalStateException("Default sensor status not found")

        val sensor = Sensor(
            homeId = homeId,
            name = request.name,
            type = sensorType,
            status = initialStatus
        )

        val savedSensor = sensorRepository.save(sensor)

        sensorType.indicators.forEach { typeIndicator ->
            val indicator = SensorIndicator(
                sensor = savedSensor,
                type = sensorType,
                name = typeIndicator.name
            )
            sensorIndicatorRepository.save(indicator)
        }

        if (sensorType.type == "temperature") eventProducer.sendSensorCreatedEvent(savedSensor)

        return mapToResponse(savedSensor)
    }

    fun getSensorsForHome(homeId: UUID): List<SensorResponse> = sensorRepository.findByHomeId(homeId)
        .map { sensor ->
            if (sensor.type?.type == "temperature") {
                val legacyData = legacyIntegrationService.getTemperatureFromLegacySystem(sensor.id.toString())
                if (legacyData != null && sensor.id != null) {
                    // Update our local telemetry with legacy data
                    updateSensorTelemetry(sensor.id!!, "temperature", legacyData.value.toString())
                }
            }
            mapToResponse(sensor)
        }

    fun getSensorState(homeId: UUID, sensorId: UUID): SensorResponse {
        val sensor = sensorRepository.findByIdAndHomeId(sensorId, homeId)
            ?: throw IllegalArgumentException("Sensor not found")

        if (sensor.type?.type == "temperature") {
            legacyIntegrationService.getTemperatureFromLegacySystem(sensorId.toString())?.let {
                updateSensorTelemetry(sensorId, "temperature", it.value.toString())
            }
        }

        return mapToResponse(sensor)
    }

    fun sendCommandToSensor(homeId: UUID, sensorId: UUID, command: SensorCommandRequest): SensorResponse {
        val sensor = sensorRepository.findByIdAndHomeId(sensorId, homeId)
            ?: throw IllegalArgumentException("Sensor not found")

        sensor.type?.commands?.find { it.command == command.command }
            ?: throw IllegalArgumentException("Command ${command.command} not supported for sensor type ${sensor.type?.type}")

        when (sensor.type?.type) {
            "temperature" -> {
                if (command.command == "calibrate") {
                    val value = command.value?.toDoubleOrNull()
                    if (value != null) {
                        legacyIntegrationService.updateTemperatureInLegacySystem(sensorId.toString(), value, "active")
                    }
                }
            }

            "movement" -> {
                if (command.command == "arm") {
                    sensor.status = sensorStatusRepository.findByStatus("on") ?: sensor.status
                } else if (command.command == "disarm") {
                    sensor.status = sensorStatusRepository.findByStatus("off") ?: sensor.status
                }
            }

            "smoke" -> {
                if (command.command == "test") {
                    eventProducer.sendSensorAlertEvent(sensor, "SMOKE_TEST", "Sensor test initiated")
                }
            }

            else -> throw IllegalStateException("Unsupported sensor type")
        }

        val updatedSensor = sensorRepository.save(sensor)
        eventProducer.sendSensorCommandEvent(updatedSensor, command.command, command.value)
        return mapToResponse(updatedSensor)
    }

    @Transactional
    fun removeSensor(homeId: UUID, sensorId: UUID) {
        val sensor = sensorRepository.findByIdAndHomeId(sensorId, homeId)
            ?: throw IllegalArgumentException("Sensor not found")

        val sensorType = sensor.type?.type
        sensorRepository.delete(sensor)

        if (sensorType == "temperature") {
            eventProducer.sendSensorDeletedEvent(sensor)
        }
    }

    fun updateSensorTelemetry(sensorId: UUID, indicatorName: String, value: String) {
        val indicator = sensorIndicatorRepository.findBySensorIdAndName(sensorId, indicatorName)
            ?: throw IllegalArgumentException("Sensor indicator with id $sensorId not found")

        val telemetry = SensorTelemetry(
            indicator = indicator,
            value = value
        )

        sensorTelemetryRepository.save(telemetry)
        val sensor: Sensor = indicator.sensor ?: throw IllegalStateException("No sensor associated with indicator")
        sensorRepository.save(sensor)
        val sensorType = sensor.type?.type

        when {
            sensorType == "temperature" && value.toDoubleOrNull()?.let { it > 35.0 } == true -> {
                eventProducer.sendSensorAlertEvent(sensor, "HIGH_TEMPERATURE", "Temperature exceeded 35°C: $value")
            }

            sensorType == "smoke" && value == "DETECTED" -> {
                eventProducer.sendSensorAlertEvent(sensor, "SMOKE_DETECTED", "Smoke detected!")
            }

            sensorType == "movement" && value == "MOVEMENT" -> {
                if (sensor.status?.status == "on") {
                    eventProducer.sendSensorAlertEvent(sensor, "MOVEMENT_DETECTED", "Movement detected while armed")
                }
            }
        }
    }

    private fun mapToResponse(sensor: Sensor): SensorResponse {
        return SensorResponse(
            id = sensor.id,
            name = sensor.name,
            type = sensor.type?.type,
            status = sensor.status?.status,
            indicators = sensor.indicators.map { indicator ->
                IndicatorResponse(
                    name = indicator.name,
                    telemetry = indicator.telemetry.sortedByDescending { it.timestamp }
                        .take(10)
                        .map { telemetry ->
                            TelemetryResponse(
                                name = indicator.name,
                                value = telemetry.value,
                                timestamp = telemetry.timestamp.toString()
                            )
                        }
                )
            }
        )
    }
}
