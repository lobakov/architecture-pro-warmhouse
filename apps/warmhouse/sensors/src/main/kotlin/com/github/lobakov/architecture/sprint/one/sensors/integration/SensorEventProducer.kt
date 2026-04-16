package com.github.lobakov.architecture.sprint.one.sensors.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.lobakov.architecture.sprint.one.sensors.model.dao.Sensor
import com.github.lobakov.architecture.sprint.one.sensors.model.dto.SensorEvent
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.time.LocalDateTime.now
import java.util.UUID

@Component
class SensorEventProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper
) {

    companion object {
        private const val TOPIC_SENSOR_EVENTS = "sensor-events"
        private const val TOPIC_NOTIFICATIONS = "notifications"
        private val LOGGER = LoggerFactory.getLogger(SensorEventProducer::class.java)
    }
    
    fun sendSensorCreatedEvent(sensor: Sensor) {
        val event = SensorEvent(
            eventId = UUID.randomUUID().toString(),
            eventType = "SENSOR_CREATED",
            sensorId = sensor.id,
            homeId = sensor.homeId,
            sensorType = sensor.type?.type,
            data = mapOf(
                "name" to (sensor.name ?: throw IllegalStateException("Sensor has no name")),
                "status" to (sensor.status?.status ?: "no_connection")
            ),
            timestamp = now()
        )
        sendEvent(TOPIC_SENSOR_EVENTS, event)
    }
    
    fun sendSensorDeletedEvent(sensor: Sensor) {
        val event = SensorEvent(
            eventId = UUID.randomUUID().toString(),
            eventType = "SENSOR_DELETED",
            sensorId = sensor.id,
            homeId = sensor.homeId,
            sensorType = sensor.type?.type,
            timestamp = now()
        )
        sendEvent(TOPIC_SENSOR_EVENTS, event)
    }
    
    fun sendSensorCommandEvent(sensor: Sensor, command: String, value: String?) {
        val event = SensorEvent(
            eventId = UUID.randomUUID().toString(),
            eventType = "SENSOR_COMMAND",
            sensorId = sensor.id,
            homeId = sensor.homeId,
            sensorType = sensor.type?.type,
            data = mapOf(
                "command" to command,
                "value" to (value ?: "")
            ),
            timestamp = now()
        )
        sendEvent(TOPIC_SENSOR_EVENTS, event)
    }
    
    fun sendSensorAlertEvent(sensor: Sensor, alertType: String, message: String) {
        val event = SensorEvent(
            eventType = "SENSOR_ALERT",
            sensorId = sensor.id,
            homeId = sensor.homeId,
            sensorType = sensor.type?.type,
            data = mapOf(
                "alertType" to alertType,
                "message" to message
            ),
            eventId = UUID.randomUUID().toString(),
            timestamp = now()
        )
        sendEvent(TOPIC_SENSOR_EVENTS, event)

        val notification = mapOf(
            "type" to "SENSOR_ALERT",
            "userId" to sensor.homeId.toString(),
            "title" to "Sensor Alert: $alertType",
            "body" to message,
            "data" to mapOf(
                "sensorId" to sensor.id.toString(),
                "sensorType" to sensor.type?.type
            )
        )
        sendEvent(TOPIC_NOTIFICATIONS,objectMapper.writeValueAsString(notification))
    }
    
    private fun sendEvent(topic: String, event: SensorEvent) {
        try {
            val json = objectMapper.writeValueAsString(event)
            kafkaTemplate.send(topic, event.sensorId.toString(), json)
        } catch (e: Exception) {
            LOGGER.warn("Failed to send event: ${e.message}")
        }
    }
    
    private fun sendEvent(topic: String, message: String) {
        try {
            kafkaTemplate.send(topic, message)
        } catch (e: Exception) {
            LOGGER.warn("Failed to send message: ${e.message}")
        }
    }
}
