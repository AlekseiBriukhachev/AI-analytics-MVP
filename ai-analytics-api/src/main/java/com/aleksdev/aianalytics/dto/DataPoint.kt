package com.aleksdev.aianalytics.dto

import io.swagger.v3.oas.annotations.media.Schema

data class DataPoint(
    @field:Schema(description = "Feature list", example = "[1.0, 2.0, 3.0]")
    val features: List<Double>,

    @field:Schema(description = "Class label", example = "1.0")
    val label: Double? = null
)
