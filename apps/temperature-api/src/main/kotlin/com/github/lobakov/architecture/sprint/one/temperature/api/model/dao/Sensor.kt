package com.github.lobakov.architecture.sprint.one.temperature.api.model.dao

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "sensors")
class Sensor(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long = 0,

    val name: String,
    val type: String,
    val location: String,
    val value: Float,
    val unit: String,
    val status: String,

    @CreationTimestamp
    val createdAt: LocalDateTime,

    @Column("last_updated")
    @UpdateTimestamp
    val updatedAt: LocalDateTime
)
