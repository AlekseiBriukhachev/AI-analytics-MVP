package com.aleksdev.aianalytics

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@EnableAsync
class AnalyticApplication {

    fun main(args: Array<String>) {
        runApplication<AnalyticApplication>(*args)
    }
}