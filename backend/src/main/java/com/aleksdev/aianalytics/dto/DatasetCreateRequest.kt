package com.aleksdev.aianalytics.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

data class DatasetCreateRequest(
    @field:NotBlank(message = "The title cannot be empty")
    @field:Size(max = 255, message = "The title cannot be longer than 255 characters")
    @Schema(description = "Dataset title", example = "Sales data")
    val name: String,

    @field:Size(max = 1000, message = "The description cannot be longer than 1000 characters")
    @Schema(description = "Dataset description", example = "2025 historical sales data")
    val description: String? = null,

    @field:NotEmpty(message = "Data cannot be empty")
    @Schema(description = "Array of data points")
    val data: List<DataPoint>,

    @Schema(description = "Feature names", example = "[\"price\", \"quantity\", \"category\"]")
    val featureNames: List<String> = emptyList()
)