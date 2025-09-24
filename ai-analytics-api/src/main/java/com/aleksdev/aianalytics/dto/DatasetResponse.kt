package com.aleksdev.aianalytics.dto

import io.swagger.v3.oas.annotations.media.Schema

data class DatasetResponse(
    @field:Schema(description = "Unique identifier")
    val id: String,

    @field:Schema(description = "Dataset name")
    val name: String,

    @field:Schema(description = "Description")
    val description: String?,

    @field:Schema(description = "Number of records")
    val recordCount: Int,

    @field:Schema(description = "Number of features")
    val featureCount: Int,

    @field:Schema(description = "Creation date")
    val createdAt: String,

    @field:Schema(description = "Feature names")
    val featureNames: List<String>?
)