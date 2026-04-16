package com.github.lobakov.architecture.sprint.one.sensors.repository

import com.github.lobakov.architecture.sprint.one.sensors.model.dao.SensorIndicator
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SensorIndicatorRepository : JpaRepository<SensorIndicator, Long> {

    fun findBySensorIdAndName(sensorId: UUID, name: String): SensorIndicator?
}
