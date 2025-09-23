package com.aleksdev.aianalytics.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank

data class TrainingRequest(
    @field:NotBlank(message = "The dataset ID cannot be empty")
    @Schema(description = "Dataset ID")
    val datasetId: String,

    @field:Valid
    @Schema(description = "Model configuration")
    val config: ModelConfig,

    @field:DecimalMin(value = "0.1", message = "Minimum test sample size: 0.1")
    @field:DecimalMax(value = "0.5", message = "Maximum test sample size: 0.5")
    @Schema(description = "Proportion of data to be tested", example = "0.2")
    val testSplit: Double = 0.2
)