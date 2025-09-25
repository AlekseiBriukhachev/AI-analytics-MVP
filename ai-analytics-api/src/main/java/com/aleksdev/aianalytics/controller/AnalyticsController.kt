package com.aleksdev.aianalytics.controller

import com.aleksdev.aianalytics.repository.DatasetRepository
import com.aleksdev.aianalytics.repository.MLModelRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@CrossOrigin(origins = ["http://localhost:3000"])
@RestController
@RequestMapping("/api/analytics")
@Tag(name = "Analytics Controller", description = "Endpoints for obtaining analytics and statistics")
class AnalyticsController(
    private val datasetRepository: DatasetRepository,
    private val modelRepository: MLModelRepository
) {

    @GetMapping("/dashboard")
    @Operation(summary = "Get data for dashboard")
    fun getDashboard(): ResponseEntity<Map<String, Any>> {
        val since24h = LocalDateTime.now().minusDays(1)
        val since7d = LocalDateTime.now().minusDays(7)

        val dashboardData = mapOf(
            "totalDatasets" to datasetRepository.count(),
            "totalModels" to modelRepository.count(),
            "datasetsLast24h" to datasetRepository.countCreatedSince(since24h),
            "modelsLast24h" to modelRepository.countCreatedSince(since24h),
            "datasetsLast7d" to datasetRepository.countCreatedSince(since7d),
            "modelsLast7d" to modelRepository.countCreatedSince(since7d),
            "recentDatasets" to datasetRepository.findTop10ByOrderByCreatedAtDesc(),
            "topModels" to modelRepository.findTop10ByOrderByAccuracyDesc()
        )

        return ResponseEntity.ok(dashboardData)
    }
}