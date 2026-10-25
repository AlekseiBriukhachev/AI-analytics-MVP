package com.aleksdev.aianalytics.dto

import io.swagger.v3.oas.annotations.media.Schema

data class PredictionResponse(
    @field:Schema(description = "Predicted value")
    val prediction: Double,

    @field:Schema(description = "Level of confidence")
    val confidence: Double,

    @field:Schema(description = "Class (for classification)")
    val predictedClass: String
)