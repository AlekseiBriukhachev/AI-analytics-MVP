package com.aleksdev.aianalytics.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty

data class ModelConfig(
    @field:NotEmpty(message = "Hidden layers cannot be empty")
    @Schema(description = "Dimensions of hidden layers", example = "[10, 5]")
    val hiddenLayers: List<Int>,

    @field:DecimalMin(value = "0.0001", message = "The learning speed should be greater than 0.0001")
    @field:DecimalMax(value = "1.0", message = "The learning speed should be less than 1.0")
    @Schema(description = "The learning speed", example = "0.01")
    val learningRate: Double = 0.01,

    @field:Min(value = 10, message = "Minimum number of epochs: 10")
    @field:Max(value = 50000, message = "Maximum number of epochs: 50000")
    @Schema(description = "Number of learning epochs", example = "1000")
    val epochs: Int = 1000,

    @Schema(description = "Activation function", example = "sigmoid")
    val activationFunction: String = "sigmoid"
)