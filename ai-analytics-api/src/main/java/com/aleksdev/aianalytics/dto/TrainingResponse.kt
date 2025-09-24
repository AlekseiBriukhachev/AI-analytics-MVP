package com.aleksdev.aianalytics.dto

import io.swagger.v3.oas.annotations.media.Schema

data class TrainingResponse(
    @field:Schema(description = "Model ID")
    val modelId: String,

    @field:Schema(description = "Accuracy on test data")
    val accuracy: Double,

    @field:Schema(description = "Value of the loss function")
    val loss: Double,

    @field:Schema(description = "Number of epochs")
    val epochs: Int,

    @field:Schema(description = "Learning time in milliseconds")
    val trainingTime: Long,

    @field:Schema(description = "Model status")
    val status: String
)