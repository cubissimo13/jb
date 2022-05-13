package com.example.jetbrains.security

import com.example.jetbrains.service.TokenService
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationFilter(private val tokenService: TokenService) : OncePerRequestFilter() {

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
        val anyOneAccessEndpoint = environment.getRequiredProperty("jetbrains.app.anyone.access")
            .split(",")
        return anyOneAccessEndpoint.contains(request.servletPath)
            || request.servletPath.startsWith("/swagger-ui")
            || request.servletPath.startsWith("/v3/api-docs")
    }

    private fun getAuthHeader(request: HttpServletRequest): String {
        return request.getHeader(AUTH_HEADER_NAME) ?: "Empty auth header"
    }
}