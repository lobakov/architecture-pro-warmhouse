package com.github.lobakov.architecture.sprint.one.sensors.controller

import com.github.lobakov.architecture.sprint.one.sensors.model.dto.SensorCommandRequest
import com.github.lobakov.architecture.sprint.one.sensors.model.dto.SensorRequest
import com.github.lobakov.architecture.sprint.one.sensors.model.dto.SensorResponse
import com.github.lobakov.architecture.sprint.one.sensors.model.dto.SensorTelemetryUpdate
import com.github.lobakov.architecture.sprint.one.sensors.service.SensorManagementService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1.0/home/{homeId}/sensors")
class SensorController(
    private val sensorService: SensorManagementService
) {

    @GetMapping
    fun getSensorsForHome(@PathVariable homeId: UUID): ResponseEntity<List<SensorResponse>> = ResponseEntity.ok(
        sensorService.getSensorsForHome(homeId)
    )

    @PutMapping
    fun addSensor(
        @PathVariable homeId: UUID,
        @Valid @RequestBody request: SensorRequest
    ): ResponseEntity<SensorResponse> = ResponseEntity.status(CREATED).body(sensorService.createSensor(homeId, request))

    @GetMapping("/{sensorId}")
    fun getSensorState(
        @PathVariable homeId: UUID,
        @PathVariable sensorId: UUID
    ): ResponseEntity<SensorResponse> = ResponseEntity.ok(sensorService.getSensorState(homeId, sensorId))

    @PostMapping("/{sensorId}")
    fun adjustSensor(
        @PathVariable homeId: UUID,
        @PathVariable sensorId: UUID,
        @Valid @RequestBody command: SensorCommandRequest
    ): ResponseEntity<SensorResponse> = ResponseEntity.ok(sensorService.sendCommandToSensor(homeId, sensorId, command))

    @PostMapping("/{sensorId}/telemetry")
    fun updateTelemetry(
        @PathVariable homeId: UUID,
        @PathVariable sensorId: UUID,
        @RequestBody update: SensorTelemetryUpdate
    ): ResponseEntity<Void> {
        sensorService.updateSensorTelemetry(sensorId, update.indicatorName, update.value)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{sensorId}")
    fun removeSensor(
        @PathVariable homeId: UUID,
        @PathVariable sensorId: UUID
    ): ResponseEntity<Void> {
        sensorService.removeSensor(homeId, sensorId)
        return ResponseEntity.ok().build()
    }
}
