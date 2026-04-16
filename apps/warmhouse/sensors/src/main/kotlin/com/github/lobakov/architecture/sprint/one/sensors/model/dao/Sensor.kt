package com.github.lobakov.architecture.sprint.one.sensors.model.dao

import jakarta.persistence.CascadeType.ALL
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.UUID
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(schema = "warmhouse", name = "sensor")
class Sensor(
    @Id
    @GeneratedValue(strategy = UUID)
    var id: UUID? = null,

    var name: String? = null,

    var homeId: UUID? = null,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sensor_type_id")
    var type: SensorType? = null,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sensor_status_id")
    var status: SensorStatus? = null,

    @OneToMany(mappedBy = "sensor", cascade = [ALL], fetch = FetchType.LAZY)
    var indicators: MutableList<SensorIndicator> = mutableListOf(),

    @CreationTimestamp
    var createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null
)
