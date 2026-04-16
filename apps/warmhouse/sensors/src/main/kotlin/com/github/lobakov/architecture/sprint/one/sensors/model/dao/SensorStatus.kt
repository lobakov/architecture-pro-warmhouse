package com.github.lobakov.architecture.sprint.one.sensors.model.dao

import jakarta.persistence.*
import jakarta.persistence.GenerationType.IDENTITY

@Entity
@Table(schema = "warmhouse", name = "sensor_status")
class SensorStatus(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long,

    val status: String
)
