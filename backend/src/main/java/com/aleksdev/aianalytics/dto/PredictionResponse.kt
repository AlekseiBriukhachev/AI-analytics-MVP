package com.aleksdev.aianalytics.dto

import io.swagger.v3.oas.annotations.media.Schema

data class PredictionResponse(
    @Schema(description = "Predicted value")
    val prediction: Double,

    @Schema(description = "Level of confidence")
    val confidence: Double,

    @Schema(description = "Class (for classification)")
    val predictedClass: String
)