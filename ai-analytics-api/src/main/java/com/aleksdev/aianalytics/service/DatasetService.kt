package com.aleksdev.aianalytics.service

import com.aleksdev.aianalytics.dto.DatasetCreateRequest
import com.aleksdev.aianalytics.dto.DataPoint
import com.aleksdev.aianalytics.dto.DatasetResponse
import com.aleksdev.aianalytics.entity.Dataset
import com.aleksdev.aianalytics.exception.ResourceNotFoundException
import com.aleksdev.aianalytics.repository.DatasetRepository
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.transaction.Transactional
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter

@Service
@Transactional
class DatasetService(
    private val datasetRepository: DatasetRepository,
    private val objectMapper: ObjectMapper
) {

    fun createDataset(request: DatasetCreateRequest): DatasetResponse {
        validateDataset(request)

        val dataset = Dataset(
            name = request.name,
            description = request.description,
            dataJson = objectMapper.writeValueAsString(request.data),
            featureNames = request.featureNames.takeIf { it.isNotEmpty() }
                ?.joinToString(","),
            recordCount = request.data.size,
            featureCount = request.data.firstOrNull()?.features?.size ?: 0
        )

        val saved = datasetRepository.save(dataset)
        return  mapToResponse(saved)
    }

    @Cacheable("datasets")
    fun getDataset(id: String): DatasetResponse {
        val dataset = datasetRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Dataset not found: $id") }
        return  mapToResponse(dataset)
    }

    fun getAllDatasets(pageable: Pageable): Page<DatasetResponse> = datasetRepository
        .findAll(pageable)
        .map { mapToResponse(it) }

    fun searchDatasets(name: String, pageable: Pageable): Page<DatasetResponse> = datasetRepository
        .findByNameContainingIgnoreCase(name, pageable)
        .map { mapToResponse(it) }

    fun getDatasetData(id: String): List<DataPoint> {
        val dataset = datasetRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Dataset not found: $id") }

        return objectMapper.readValue(
            dataset.dataJson,
            objectMapper.typeFactory.constructCollectionType(List::class.java, DataPoint::class.java)
        )
    }

    fun deleteDataset(id: String) {
        if (!datasetRepository.existsById(id)) {
            throw ResourceNotFoundException("Dataset not found: $id")
        }
        datasetRepository.deleteById(id)
    }

    private fun validateDataset(request: DatasetCreateRequest) {
        require (request.data.isNotEmpty()) {
            throw  IllegalArgumentException("Dataset cannot be empty")
        }

        val featureCount = request.data.first().features.size
        require (featureCount != 0) {
            throw IllegalArgumentException("Features cannot be empty")
        }

        request.data.forEach { point ->
            require (point.features.size == featureCount) {
                throw IllegalArgumentException("Inconsistent feature count across data points")
            }
        }

        val hasLabels = request.data.all { it.label != null }
        val hasNotLabels = request.data.all { it.label == null }

        require (!(!hasLabels && !hasNotLabels)) {
            throw IllegalArgumentException("Either all data points must have labels or none")
        }
    }

    private fun mapToResponse(dataset: Dataset): DatasetResponse = DatasetResponse(
        id = dataset.id!!,
        name = dataset.name,
        description = dataset.description,
        recordCount = dataset.recordCount,
        featureCount = dataset.featureCount,
        createdAt = dataset.createdAt!!.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        featureNames = dataset.featureNames?.split(",")
    )
}