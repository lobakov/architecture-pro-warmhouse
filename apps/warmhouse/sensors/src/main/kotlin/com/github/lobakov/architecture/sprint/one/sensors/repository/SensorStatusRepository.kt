package com.github.lobakov.architecture.sprint.one.sensors.repository

import com.github.lobakov.architecture.sprint.one.sensors.model.dao.SensorStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SensorStatusRepository : JpaRepository<SensorStatus, Long> {

    fun findByStatus(status: String): SensorStatus?
}
