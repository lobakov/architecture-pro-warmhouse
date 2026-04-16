package com.github.lobakov.architecture.sprint.one.sensors.model.dao

import jakarta.persistence.Entity
import jakarta.persistence.FetchType.LAZY
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(schema = "warmhouse", name = "sensor_type")
class SensorType(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    var id: Long,

    var type: String,

    @OneToMany(mappedBy = "type", fetch = LAZY)
    var commands: MutableList<SensorCommand> = mutableListOf(),

    @OneToMany(mappedBy = "type", fetch = LAZY)
    var indicators: MutableList<SensorTypeIndicator> = mutableListOf()
)
