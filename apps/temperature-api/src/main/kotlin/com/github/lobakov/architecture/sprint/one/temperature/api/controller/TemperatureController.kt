package com.github.lobakov.architecture.sprint.one.temperature.api.controller

import com.github.lobakov.architecture.sprint.one.temperature.api.model.dto.TemperatureResponse
import com.github.lobakov.architecture.sprint.one.temperature.api.service.TemperatureService
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/temperature")
class TemperatureController(
    private val temperatureService: TemperatureService
) {

    @GetMapping(produces = [APPLICATION_JSON_VALUE])
    fun getTemperature(
        @RequestParam("location", required = true) location: String
    ): ResponseEntity<TemperatureResponse> = ResponseEntity.ok(temperatureService.getTemperature(location))
}
