package com.github.lobakov.architecture.sprint.one.temperature.api.repository

import com.github.lobakov.architecture.sprint.one.temperature.api.model.dao.Sensor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SensorRepository : JpaRepository<Sensor, Long> {

    fun findAllByLocation(location: String): Set<Sensor>
}
