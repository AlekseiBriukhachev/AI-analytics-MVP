package com.aleksdev.aianalytics.service

import com.aleksdev.aianalytics.constants.ModelStatus
import com.aleksdev.aianalytics.dto.ModelConfig
import com.aleksdev.aianalytics.dto.PredictionRequest
import com.aleksdev.aianalytics.dto.PredictionResponse
import com.aleksdev.aianalytics.dto.TrainingRequest
import com.aleksdev.aianalytics.dto.TrainingResponse
import com.aleksdev.aianalytics.entity.MLModel
import com.aleksdev.aianalytics.exception.ResourceNotFoundException
import com.aleksdev.aianalytics.ml.NeuralNetworkEngine
import com.aleksdev.aianalytics.repository.MLModelRepository
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.transaction.Transactional
import org.springframework.cache.annotation.Cacheable
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture

@Service
@Transactional
class MLModelService(
    private val modelRepository: MLModelRepository,
    private val datasetService: DatasetService,
    private val neuralNetworkEngine: NeuralNetworkEngine,
    private val objectMapper: ObjectMapper
) {

    @Async
    fun trainModelAsync(request: TrainingRequest): CompletableFuture<TrainingResponse> =
        CompletableFuture.supplyAsync {
            trainModel(request)
        }

    fun trainModel(request: TrainingRequest): TrainingResponse {
        // Get data
        val dataPoints = datasetService.getDatasetData(request.datasetId)

        // Checking for labels
        if (dataPoints.any { it.label == null }) {
            throw IllegalArgumentException("All data points must have labels for supervised learning")
        }

        // Prepare dates
        val shuffledData = dataPoints.shuffled()
        val splitIndex = (shuffledData.size * (1 - request.testSplit)).toInt()
        val trainData = shuffledData.take(splitIndex).map { it.features to it.label }
        val testData = shuffledData.drop(splitIndex).map { it.features to it.label }

        // Create the network architecture
        val inputSize = dataPoints.first().features.size
        val layers = listOf(inputSize) + request.config.hiddenLayers + listOf(1)

        // Create the model in DB with status TRAINING
        val model = MLModel(
            datasetId = request.datasetId,
            modelConfigJson = objectMapper.writeValueAsString(request.config),
            modelWeightsJson = "",
            status = ModelStatus.TRAINING
        )
        val savedModel = modelRepository.save(model)

        try {
            // Learn the network
            val result = neuralNetworkEngine.createAndTrain(
                layers = layers,
                trainData = trainData,
                testData = testData,
                learningRate = request.config.learningRate,
                epochs = request.config.epochs
            )

            // Update the model
            savedModel.apply {
                modelWeightsJson = result.weightsJson
                accuracy = result.accuracy
                loss = result.loss
                epochs = request.config.epochs
                trainingTime = result.trainingTime
                status = ModelStatus.TRAINED
            }

            modelRepository.save(savedModel)

            return TrainingResponse(
                modelId = savedModel.id!!,
                accuracy = result.accuracy,
                loss = result.loss,
                epochs = request.config.epochs,
                trainingTime = result.trainingTime,
                status = ModelStatus.TRAINED.name
            )
        } catch (e: Exception) {
            // Update model status to ERROR
            savedModel.status = ModelStatus.ERROR
            modelRepository.save(savedModel)
            throw e
        }
    }

    @Cacheable("models")
    fun getModel(id: String): MLModel = modelRepository
        .findById(id)
        .orElseThrow { ResourceNotFoundException("Model not found: $id")  }

    fun predict(request: PredictionRequest): PredictionResponse {
        val model = getModel(request.modelId)

        if (model.status != ModelStatus.TRAINED) {
            throw IllegalArgumentException("Model is not trained yet: ${request.modelId}")
        }

        // Restore model configuration
        val config = objectMapper.readValue(model.modelConfigJson, ModelConfig::class.java)
        val dataset = datasetService.getDataset(model.datasetId)

        val inputSize = dataset.featureCount
        val layers = listOf(inputSize) + config.hiddenLayers + listOf(1)

        // Weight loading
        val network = neuralNetworkEngine.loadNetwork(layers, model.modelWeightsJson, config.learningRate)

        // We make a prediction
        val prediction = network.predict(request.features.toDoubleArray())
        val confidence = kotlin.math.abs(prediction - 0.5) * 2 // A simple confidence metric
        val predictedClass = if (prediction > 0.5) "Positive (1)" else "Negative (0)"

        return PredictionResponse(
            prediction = prediction,
            confidence = confidence,
            predictedClass = predictedClass
        )
    }

    fun getAllModels(): List<MLModel> = modelRepository.findAll()

    fun getModelsByDataset(datasetId: String): List<MLModel> = modelRepository.findByDatasetId(datasetId)

    fun deleteModel(id: String) {
        val model = getModel(id)
        model.status = ModelStatus.DELETED
        modelRepository.save(model)
    }
}