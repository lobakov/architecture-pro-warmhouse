package com.github.lobakov.architecture.sprint.one.sensors.repository

import com.github.lobakov.architecture.sprint.one.sensors.model.dao.SensorType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SensorTypeRepository : JpaRepository<SensorType, Long> {

    fun findByType(type: String): SensorType?
}
