package com.example.jetbrains.security.config

import com.example.jetbrains.security.AuthenticationFilter
import com.example.jetbrains.security.AuthorizationInterceptor
import com.example.jetbrains.service.TokenService
import com.example.jetbrains.service.UserRoleCacheService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class SecurityConfig(private val userRoleCacheService: UserRoleCacheService): WebMvcConfigurer {

    @Bean
    fun authSecurityFilter(tokenService: TokenService): AuthenticationFilter {
        return AuthenticationFilter(tokenService)
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(AuthorizationInterceptor(userRoleCacheService))
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000")
            .allowedHeaders("*")
            .allowedMethods("*")
            .allowCredentials(true)
    }
}