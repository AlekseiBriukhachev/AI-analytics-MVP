package com.aleksdev.aianalytics.ml

import kotlin.math.exp
import kotlin.math.sqrt
import kotlin.random.Random

class AdvancedNeuralNetwork(
    private val layers: List<Int>,
    private val learningRate: Double = 0.01,
    private val activationFunction: String = "sigmoid"
) {
    private val weights: List<Array<DoubleArray>>
    private val biases: List<DoubleArray>
    private val random = Random.Default

    init {
        weights = mutableListOf<Array<DoubleArray>>().apply {
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

        biases = layers.drop(1).map { size ->
            DoubleArray(size) { random.nextGaussian() * 0.1 }
        }
    }

    private fun activate(x: Double): Double = when (activationFunction) {
        "sigmoid" -> 1.0 / (1.0 + exp(-x))
        "relu" -> maxOf(0.0, x)
        "tanh" -> kotlin.math.tanh(x)
        else -> 1.0 / (1.0 + exp(-x))
    }
}