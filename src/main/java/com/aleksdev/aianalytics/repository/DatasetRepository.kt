package com.aleksdev.aianalytics.repository

import com.aleksdev.aianalytics.entity.Dataset
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface DatasetRepository : JpaRepository<Dataset, String> {
    fun findByNameContainingIgnoreCase(name: String, pageable: Pageable): Page<Dataset>

    @Query("SELECT COUNT(d) FROM d WHERE d.createdAt >= :since")
    fun countCreatedSince(since: LocalDateTime): Long

    fun findTop10ByOrderByCreatedAtDesc(): List<Dataset>
}