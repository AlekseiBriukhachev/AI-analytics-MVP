package com.aleksdev.aianalytics.controller

import com.aleksdev.aianalytics.dto.ApiResponse
import com.aleksdev.aianalytics.dto.DataPoint
import com.aleksdev.aianalytics.dto.DatasetCreateRequest
import com.aleksdev.aianalytics.dto.DatasetResponse
import com.aleksdev.aianalytics.service.DatasetService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/datasets")
@Tag(name = "Datasets", description = "API for datasets management")
class DatasetController(
    private val datasetService: DatasetService
) {

    @PostMapping
    @Operation(summary = "Create new dataset", description = "Downloads a new dataset for model training")
    fun createDataset(@Valid @RequestBody request: DatasetCreateRequest): ResponseEntity<ApiResponse<DatasetResponse>> {
        val dataset = datasetService.createDataset(request)
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse(success = true, data = dataset))
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get dataset by ID")
    fun getDataset(@Parameter(description = "Dataset ID") @PathVariable id: String): ResponseEntity<ApiResponse<DatasetResponse>> {
        val dataset = datasetService.getDataset(id)
        return ResponseEntity.ok(ApiResponse(success = true, data = dataset))
    }

    @GetMapping
    @Operation(summary = "Get all datasets", description = "Returns a page-by-page list of all datasets")
    fun getAllDatasets(
        @Parameter(description = "Parameters of pagination")
        @PageableDefault(size = 20) pageable: Pageable,
        @Parameter(description = "Searching by name")
        @RequestParam(required = false) search: String?
    ): ResponseEntity<ApiResponse<Page<DatasetResponse>>> {
        val datasets = if (search.isNullOrBlank()) {
            datasetService.getAllDatasets(pageable)
        } else {
            datasetService.searchDatasets(search, pageable)
        }
        return ResponseEntity.ok(ApiResponse(success = true, data = datasets))
    }

    @GetMapping("/{id}/data")
    @Operation(summary = "Get data of dataset", description = "Return raw data of dataset")
    fun getDatasetData(@PathVariable id: String): ResponseEntity<ApiResponse<List<DataPoint>>> {
        val data = datasetService.getDatasetData(id)
        return ResponseEntity.ok(ApiResponse(success = true, data = data))
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete dataset")
    fun deleteDataset(@PathVariable id: String): ResponseEntity<ApiResponse<String>> {
        datasetService.deleteDataset(id)
        return ResponseEntity.ok(ApiResponse(success = true, data = "Dataset deleted successfully"))
    }
}