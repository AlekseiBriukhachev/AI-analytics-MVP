package com.aleksdev.aianalytics.controller

import com.aleksdev.aianalytics.repository.DatasetRepository
import com.aleksdev.aianalytics.repository.MLModelRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/analytics")
@Tag(name = "Analytics Controller", description = "Endpoints for obtaining analytics and statistics")
class AnalyticsController(
    private val datasetRepository: DatasetRepository,
    private val modelRepository: MLModelRepository
) {
}