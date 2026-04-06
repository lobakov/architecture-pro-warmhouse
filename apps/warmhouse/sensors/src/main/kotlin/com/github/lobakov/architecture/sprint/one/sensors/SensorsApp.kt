package com.github.lobakov.architecture.sprint.one.sensors

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SensorsApp

fun main(args: Array<String>) {
    runApplication<SensorsApp>(*args)
}
