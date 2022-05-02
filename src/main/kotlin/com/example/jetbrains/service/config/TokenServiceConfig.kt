package com.example.jetbrains.service.config

import com.example.jetbrains.repository.BlackListRepository
import com.example.jetbrains.service.TokenService
import com.example.jetbrains.service.TokenServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TokenServiceConfig {
    @Bean
    fun tokenService(blackListRepository: BlackListRepository): TokenService {
        return TokenServiceImpl(blackListRepository)
    }
}