package com.example.jetbrains.service.config

import com.example.jetbrains.repository.BlackListRepository
import com.example.jetbrains.repository.UserRepository
import com.example.jetbrains.service.TokenService
import com.example.jetbrains.service.UserService
import com.example.jetbrains.service.UserServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UserServiceConfig {

    @Bean
    fun userService(
        userRepository: UserRepository,
        tokenService: TokenService,
        blackListRepository: BlackListRepository
    ): UserService {
        return UserServiceImpl(
            userRepository = userRepository,
            tokenService = tokenService,
            blackListRepository = blackListRepository
        )
    }
}