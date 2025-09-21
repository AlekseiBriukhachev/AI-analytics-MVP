package com.aleksdev.aianalytics.dto

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse<T>(
    @Schema(description = "Success of the operation", example = "true")
    val success: Boolean,

    @Schema(description = "Response data")
    val data: T? = null,

    @Schema(description = "Error message")
    val error: String? = null,

    @Schema(description = "Timestamp")
    val timestamp: Long = System.currentTimeMillis()
)