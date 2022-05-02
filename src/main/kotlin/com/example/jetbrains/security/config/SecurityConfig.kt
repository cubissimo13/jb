package com.example.jetbrains.security.config

import com.example.jetbrains.repository.UserRepository
import com.example.jetbrains.security.AuthenticationInterceptor
import com.example.jetbrains.security.AuthorizationFilter
import com.example.jetbrains.service.TokenService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class SecurityConfig(private val userRepository: UserRepository): WebMvcConfigurer {

    @Bean
    fun authSecurityFilter(tokenService: TokenService): AuthorizationFilter {
        return AuthorizationFilter(tokenService)
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(AuthenticationInterceptor(userRepository))
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000")
            .allowedHeaders("*")
            .allowedMethods("*")
            .allowCredentials(true)
    }
}