package com.github.lobakov.architecture.sprint.one.sensors.model.dao

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(schema = "warmhouse", name = "sensor_telemetry")
class SensorTelemetry(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    var id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "sensor_indicator_id")
    var indicator: SensorIndicator? = null,

    var value: String? = null,

    var timestamp: LocalDateTime? = null
)
