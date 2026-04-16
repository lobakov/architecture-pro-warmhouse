package com.github.lobakov.architecture.sprint.one.sensors.repository

import com.github.lobakov.architecture.sprint.one.sensors.model.dao.SensorCommand
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SensorCommandRepository : JpaRepository<SensorCommand, Long>