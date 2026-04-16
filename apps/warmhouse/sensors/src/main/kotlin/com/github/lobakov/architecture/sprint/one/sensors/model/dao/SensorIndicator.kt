package com.github.lobakov.architecture.sprint.one.sensors.model.dao

import jakarta.persistence.CascadeType.ALL
import jakarta.persistence.Entity
import jakarta.persistence.FetchType.LAZY
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(schema = "warmhouse", name = "sensor_indicator")
class SensorIndicator(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    var id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "sensor_id")
    var sensor: Sensor? = null,

    @ManyToOne
    @JoinColumn(name = "sensor_type_id")
    var type: SensorType? = null,

    var name: String? = null,

    @OneToMany(mappedBy = "indicator", cascade = [ALL], fetch = LAZY)
    var telemetry: MutableList<SensorTelemetry> = mutableListOf()
)
