package com.github.lobakov.architecture.sprint.one.temperature.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TemperatureApiApp

fun main(args: Array<String>) {
    runApplication<TemperatureApiApp>(*args)
}
