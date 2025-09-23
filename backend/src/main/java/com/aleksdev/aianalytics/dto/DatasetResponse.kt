package com.aleksdev.aianalytics.dto

import io.swagger.v3.oas.annotations.media.Schema

data class DatasetResponse(
    @Schema(description = "Unique identifier")
    val id: String,

    @Schema(description = "Dataset name")
    val name: String,

    @Schema(description = "Description")
    val description: String?,

    @Schema(description = "Number of records")
    val recordCount: Int,

    @Schema(description = "Number of features")
    val featureCount: Int,

    @Schema(description = "Creation date")
    val createdAt: String,

    @Schema(description = "Feature names")
    val featureNames: List<String>?
)