package com.aleksdev.aianalytics.dto

import io.swagger.v3.oas.annotations.media.Schema

data class TrainingResponse(
    @Schema(description = "Model ID")
    val modelId: String,

    @Schema(description = "Accuracy on test data")
    val accuracy: Double,

    @Schema(description = "Value of the loss function")
    val loss: Double,

    @Schema(description = "Number of epochs")
    val epochs: Int,

    @Schema(description = "Learning time in milliseconds")
    val trainingTime: Long,

    @Schema(description = "Model status")
    val status: String
)