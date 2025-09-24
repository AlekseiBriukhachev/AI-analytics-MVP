package com.aleksdev.aianalytics.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }
            .headers { headers ->
                headers.frameOptions { it.sameOrigin() }
            }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/", "/api/health", "/swagger-ui/**", "/v3/api-docs/**", "/h2-console/**", "/actuator/**").permitAll()
                    .requestMatchers("/api/**").permitAll()
                    .anyRequest().authenticated()
            }
        return http.build()
    }
}