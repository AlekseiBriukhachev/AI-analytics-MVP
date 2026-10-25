package com.aleksdev.aianalytics.controller

import com.aleksdev.aianalytics.dto.ApiResponse
import com.aleksdev.aianalytics.dto.PredictionRequest
import com.aleksdev.aianalytics.dto.PredictionResponse
import com.aleksdev.aianalytics.dto.TrainingRequest
import com.aleksdev.aianalytics.dto.TrainingResponse
import com.aleksdev.aianalytics.entity.MLModel
import com.aleksdev.aianalytics.service.MLModelService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["http://localhost:3000"])
@RestController
@RequestMapping("/api/models")
@Tag(name = "ML Model Controller", description = "Endpoints for managing machine learning models")
class MLModelController(
    private val modelService: MLModelService
) {

    @PostMapping("/train")
    @Operation(summary = "Train a new machine learning model", description = "Initiates the training of a new machine learning model with predefined parameters.")
    fun trainModel(@Valid @RequestBody request: TrainingRequest): ResponseEntity<ApiResponse<TrainingResponse>> {
        val result = modelService.trainModel(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse(success = true, data = result))
    }

    @PostMapping("/train-async")
    @Operation(summary = "Asynchronously train a new machine learning model", description = "Initiates the asynchronous training of a new machine learning model with predefined parameters.")
    fun trainModelAsync(@Valid @RequestBody request: TrainingRequest): ResponseEntity<ApiResponse<String>> {
        modelService.trainModelAsync(request)
        return ResponseEntity.accepted().body(ApiResponse(success = true, data = "Training started asynchronously"))
    }

    @PostMapping("/predict")
    @Operation(summary = "Make predictions using the trained model", description = "Uses the trained machine learning model to make predictions based on input data.")
    fun predict(@Valid @RequestBody request: PredictionRequest): ResponseEntity<ApiResponse<PredictionResponse>> {
        val predictions = modelService.predict(request)
        return ResponseEntity.ok(ApiResponse(success = true, data = predictions))
    }

    @GetMapping
    @Operation(summary = "Get all models")
    fun getAllModels(): ResponseEntity<ApiResponse<List<MLModel>>> {
        val models = modelService.getAllModels()
        return ResponseEntity.ok(ApiResponse(success = true, data = models))
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get model by ID")
    fun getModel(@PathVariable id: String): ResponseEntity<ApiResponse<MLModel>> {
        val model = modelService.getModel(id)
        return ResponseEntity.ok(ApiResponse(success = true, data = model))
    }

    @GetMapping("/by-dataset/{datasetId}")
    @Operation(summary = "Get models by dataset ID")
    fun getModelsByDataset(@PathVariable datasetId: String): ResponseEntity<ApiResponse<List<MLModel>>> {
        val models = modelService.getModelsByDataset(datasetId)
        return ResponseEntity.ok(ApiResponse(success = true, data = models))
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete model by ID")
    fun deleteModel(@PathVariable id: String): ResponseEntity<ApiResponse<String>> {
        modelService.deleteModel(id)
        return ResponseEntity.ok(ApiResponse(success = true, data = "Model deleted successfully"))
    }
}