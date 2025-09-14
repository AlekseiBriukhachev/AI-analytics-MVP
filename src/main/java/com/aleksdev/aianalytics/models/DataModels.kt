package com.aleksdev.aianalytics.models

import kotlinx.serialization.Serializable

@Serializable
data class DataPoint(
    val features: List<Double>,
    val label: Double? = null
)

@Serializable
data class Dataset(
    val name: String,
    val description: String,
    val data: List<DataPoint>,
    val featureNames: List<String> = emptyList()
)

@Serializable
data class ModelConfig(
    val hiddenLayers: List<Int> = listOf(10, 5),
    val learningRate: Double = 0.01,
    val epochs: Int = 1000,
    val activationFunction: String = "sigmoid"
)

@Serializable
data class TrainingRequest(
    val datasetId: String,
    val config: ModelConfig,
    val testSplit: Double = 0.2
)

@Serializable
data class TrainingResult(
    val modelId: String,
    val accuracy: Double,
    val loss: Double,
    val epochs: Int,
    val trainingTime: Long
)

@Serializable
data class PredictionRequest(
    val modelId: String,
    val features: List<Double>
)

@Serializable
data class PredictionResult(
    val prediction: Double,
    val confidence: Double
)

@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: String? = null
)