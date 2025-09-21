package com.aleksdev.aianalytics.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty

data class PredictionRequest(
    @field:NotBlank(message = "The model ID cannot be empty")
    @Schema(description = "Model ID")
    val modelId: String,

    @field:NotEmpty(message = "Signs cannot be empty")
    @Schema(description = "Input features", example = "[1.0, 2.0, 3.0]")
    val features: List<Double>
)