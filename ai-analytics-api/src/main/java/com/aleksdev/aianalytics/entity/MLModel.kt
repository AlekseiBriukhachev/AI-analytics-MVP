package com.aleksdev.aianalytics.entity

import com.aleksdev.aianalytics.constants.ModelStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "ml_models")
@EntityListeners(AuditingEntityListener::class)
data class MLModel(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,

    @Column(name = "dataset_id", nullable = false)
    var datasetId: String,

    @Column(name = "model_config", columnDefinition = "TEXT")
    var modelConfigJson: String,

    @Column(name = "model_weights", columnDefinition = "TEXT")
    var modelWeightsJson: String,

    @Column(name = "accuracy")
    var accuracy: Double = 0.0,

    @Column(name = "loss_value")
    var loss: Double = 0.0,

    @Column(name = "epochs")
    var epochs: Int = 0,

    @Column(name = "training_time_ms")
    var trainingTime: Long = 0,

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: ModelStatus = ModelStatus.TRAINING,

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null,

    @LastModifiedDate
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null
)
