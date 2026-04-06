package com.github.lobakov.architecture.sprint.one.sensors.model.dao

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(schema = "warmhouse", name = "sensor_type_indicator")
class SensorTypeIndicator(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    var id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "sensor_type_id")
    var type: SensorType? = null,

    var name: String? = null
)
