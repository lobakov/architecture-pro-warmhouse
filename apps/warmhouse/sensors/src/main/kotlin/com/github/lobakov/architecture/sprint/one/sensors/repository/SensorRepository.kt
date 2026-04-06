package com.github.lobakov.architecture.sprint.one.sensors.repository

import com.github.lobakov.architecture.sprint.one.sensors.model.dao.Sensor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SensorRepository : JpaRepository<Sensor, UUID> {

    fun findByIdAndHomeId(id: UUID, homeId: UUID): Sensor?

    fun findByHomeId(homeId: UUID): List<Sensor>
}
