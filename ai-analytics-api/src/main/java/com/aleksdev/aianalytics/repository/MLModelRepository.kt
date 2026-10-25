package com.aleksdev.aianalytics.repository

import com.aleksdev.aianalytics.constants.ModelStatus
import com.aleksdev.aianalytics.entity.MLModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface MLModelRepository : JpaRepository<MLModel, String> {
    fun findByDatasetId(datasetId: String): List<MLModel>
    fun findByStatus(status: ModelStatus): List<MLModel>
    fun findByDatasetIdAndStatus(dataset: String, status: ModelStatus): List<MLModel>

    @Query("SELECT COUNT(m) FROM MLModel m WHERE m.createdAt >= :since")
    fun countCreatedSince(since: LocalDateTime): Long

    fun findTop10ByOrderByAccuracyDesc(): List<MLModel>
}