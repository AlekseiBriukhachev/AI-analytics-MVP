package com.aleksdev.aianalytics

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.cache.annotation.EnableCaching
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@EnableAsync
class AnalyticApplication : SpringBootServletInitializer() {

    override fun configure(builder: SpringApplicationBuilder): SpringApplicationBuilder {
        return configureApplication(builder)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<AnalyticApplication>(*args)
        }

        private fun configureApplication(builder: SpringApplicationBuilder): SpringApplicationBuilder {
            return builder.sources(AnalyticApplication::class.java)
        }
    }
}