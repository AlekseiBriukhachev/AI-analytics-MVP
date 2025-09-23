package com.aleksdev.aianalytics.controller

import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class HealthController : HealthIndicator {

    @GetMapping("/health")
    fun healthCheck(): ResponseEntity<Map<String, Any>> {
        val health = mapOf(
            "status" to "UP",
            "timestamp" to System.currentTimeMillis(),
            "version" to "1.0.0",
            "service" to "AI Analytics API",
            "description" to "Service is running"
        )
        return ResponseEntity.ok(health)
    }

    override fun health(): Health = Health.up().withDetail("service", "AI Analytics API").build()
}