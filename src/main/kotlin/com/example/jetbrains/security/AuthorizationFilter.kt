package com.example.jetbrains.security

import com.example.jetbrains.service.TokenService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthorizationFilter(private val tokenService: TokenService) : OncePerRequestFilter() {
    private val anyOneAccessEndpoint = setOf("/api/v1/auth/register", "/api/v1/auth/login")

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = getAuthHeader(request)
        val jwtToken = tokenService.getJwtTokenFromHeader(authHeader)
        if (!tokenService.validateToken(jwtToken)) {
            logger.info("Authorization error ${request.servletPath}")
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
        } else {
            val username = tokenService.getUserName(jwtToken)
            request.setAttribute(USER_NAME_ATR, username)
            logger.info("User $username successful authorized")
            filterChain.doFilter(request, response)
        }
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return anyOneAccessEndpoint.contains(request.servletPath)
            || request.servletPath.startsWith("/swagger-ui")
            || request.servletPath.startsWith("/v3/api-docs")
    }

    private fun getAuthHeader(request: HttpServletRequest): String {
        return request.getHeader(AUTH_HEADER_NAME) ?: "Empty auth header"
    }
}