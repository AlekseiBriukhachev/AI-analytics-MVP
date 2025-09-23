package com.aleksdev.aianalytics.ml

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import java.util.*
import kotlin.math.exp
import kotlin.math.sqrt

@Component
class NeuralNetworkEngine {
    private val objectMapper = ObjectMapper()

    fun createAndTrain(
        layers: List<Int>,
        trainData: List<Pair<List<Double>, Double>>,
        testData: List<Pair<List<Double>, Double>>,
        learningRate: Double,
        epochs: Int
    ): TrainingResult {

        val network = NeuralNetwork(layers, learningRate)
        val startTime = System.currentTimeMillis()

        // Подготовка данных
        val trainInputs = trainData.map { it.first.toDoubleArray() }
        val trainTargets = trainData.map { doubleArrayOf(it.second) }

        // Обучение
        val finalLoss = network.train(trainInputs, trainTargets, epochs)

        // Тестирование
        val accuracy = calculateAccuracy(network, testData)
        val trainingTime = System.currentTimeMillis() - startTime

        // Сериализация весов
        val weightsJson = serializeWeights(network)

        return TrainingResult(
            network = network,
            accuracy = accuracy,
            loss = finalLoss,
            trainingTime = trainingTime,
            weightsJson = weightsJson
        )
    }

    fun loadNetwork(layers: List<Int>, weightsJson: String, learningRate: Double): NeuralNetwork {
        val network = NeuralNetwork(layers, learningRate)
        deserializeWeights(network, weightsJson)
        return network
    }

    private fun calculateAccuracy(network: NeuralNetwork, testData: List<Pair<List<Double>, Double>>): Double {
        var correct = 0
        testData.forEach { (features, label) ->
            val prediction = network.predict(features.toDoubleArray())
            val predictedClass = if (prediction > 0.5) 1.0 else 0.0
            if (predictedClass == label) correct++
        }
        return if (testData.isNotEmpty()) correct.toDouble() / testData.size else 0.0
    }

    private fun serializeWeights(network: NeuralNetwork): String {
        val weights = network.getWeights()
        return objectMapper.writeValueAsString(weights)
    }

    private fun deserializeWeights(network: NeuralNetwork, weightsJson: String) {
        val weights = objectMapper.readValue(weightsJson, NetworkWeights::class.java)
        network.setWeights(weights)
    }
}

data class TrainingResult(
    val network: NeuralNetwork,
    val accuracy: Double,
    val loss: Double,
    val trainingTime: Long,
    val weightsJson: String
)

data class NetworkWeights(
    val weights: List<List<List<Double>>>,
    val biases: List<List<Double>>
)

class NeuralNetwork(
    private val layers: List<Int>,
    private val learningRate: Double
) {
    private var weights: List<Array<DoubleArray>>
    private var biases: List<DoubleArray>
    private val random = Random()

    init {
        weights = initializeWeights()
        biases = initializeBiases()
    }

    private fun initializeWeights(): List<Array<DoubleArray>> {
        return mutableListOf<Array<DoubleArray>>().apply {
            for (i in 0 until layers.size - 1) {
                val layerWeights = Array(layers[i]) { DoubleArray(layers[i + 1]) }
                val xavier = sqrt(2.0 / layers[i])
                for (j in 0 until layers[i]) {
                    for (k in 0 until layers[i + 1]) {
                        layerWeights[j][k] = random.nextGaussian() * xavier
                    }
                }
                add(layerWeights)
            }
        }
    }

    private fun initializeBiases(): List<DoubleArray> {
        return layers.drop(1).map { size ->
            DoubleArray(size) { random.nextGaussian() * 0.1 }
        }
    }

    private fun sigmoid(x: Double): Double = 1.0 / (1.0 + exp(-x.coerceIn(-500.0, 500.0)))

    private fun sigmoidDerivative(x: Double): Double = x * (1 - x)

    fun predict(input: DoubleArray): Double {
        val activations = feedForward(input)
        return activations.last()[0]
    }

    private fun feedForward(input: DoubleArray): List<DoubleArray> {
        val activations = mutableListOf<DoubleArray>()
        var currentInput = input
        activations.add(currentInput)

        for (layerIndex in weights.indices) {
            val layerOutput = DoubleArray(layers[layerIndex + 1])
            for (j in layerOutput.indices) {
                layerOutput[j] = biases[layerIndex][j]
                for (i in currentInput.indices) {
                    layerOutput[j] += currentInput[i] * weights[layerIndex][i][j]
                }
                layerOutput[j] = sigmoid(layerOutput[j])
            }
            currentInput = layerOutput
            activations.add(currentInput)
        }

        return activations
    }

    fun train(inputs: List<DoubleArray>, targets: List<DoubleArray>, epochs: Int): Double {
        var finalLoss = 0.0

        repeat(epochs) { epoch ->
            var epochLoss = 0.0

            for (i in inputs.indices) {
                val activations = feedForward(inputs[i])
                val output = activations.last()

                // Вычисление ошибки
                val loss = targets[i].mapIndexed { j, target ->
                    val error = target - output[j]
                    epochLoss += error * error
                    error
                }.toDoubleArray()

                // Обратное распространение
                backpropagate(activations, loss)
            }

            if (epoch == epochs - 1) {
                finalLoss = epochLoss / inputs.size
            }
        }

        return finalLoss
    }

    private fun backpropagate(activations: List<DoubleArray>, outputError: DoubleArray) {
        val errors = mutableListOf<DoubleArray>()

        // Ошибка выходного слоя
        val outputGradient = outputError.mapIndexed { i, error ->
            error * sigmoidDerivative(activations.last()[i])
        }.toDoubleArray()

        errors.add(0, outputGradient)

        // Ошибки скрытых слоев
        for (layerIndex in weights.size - 2 downTo 0) {
            val layerError = DoubleArray(layers[layerIndex + 1])
            for (j in layerError.indices) {
                layerError[j] = 0.0
                for (k in errors[0].indices) {
                    layerError[j] += errors[0][k] * weights[layerIndex + 1][j][k]
                }
                layerError[j] *= sigmoidDerivative(activations[layerIndex + 1][j])
            }
            errors.add(0, layerError)
        }

        // Обновление весов и смещений
        for (layerIndex in weights.indices) {
            for (i in weights[layerIndex].indices) {
                for (j in weights[layerIndex][i].indices) {
                    weights[layerIndex][i][j] += learningRate * errors[layerIndex][j] * activations[layerIndex][i]
                }
            }

            for (j in biases[layerIndex].indices) {
                biases[layerIndex][j] += learningRate * errors[layerIndex][j]
            }
        }
    }

    fun getWeights(): NetworkWeights {
        return NetworkWeights(
            weights = weights.map { layer ->
                layer.map { neuron -> neuron.toList() }
            },
            biases = biases.map { it.toList() }
        )
    }

    fun setWeights(networkWeights: NetworkWeights) {
        weights = networkWeights.weights.mapIndexed { layerIndex, layer ->
            Array(layer.size) { i ->
                layer[i].toDoubleArray()
            }
        }
        biases = networkWeights.biases.map { it.toDoubleArray() }
    }
}