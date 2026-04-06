package com.github.lobakov.architecture.sprint.one.sensors.repository

import com.github.lobakov.architecture.sprint.one.sensors.model.dao.SensorTypeIndicator
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SensorTypeIndicatorRepository : JpaRepository<SensorTypeIndicator, Long>
