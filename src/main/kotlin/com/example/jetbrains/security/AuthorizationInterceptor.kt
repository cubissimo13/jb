package com.example.jetbrains.security

import com.example.jetbrains.service.UserRoleCacheService
import org.slf4j.LoggerFactory
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthorizationInterceptor(private val userRoleCacheService: UserRoleCacheService) : HandlerInterceptor {
    private val logger = LoggerFactory.getLogger(AuthorizationInterceptor::class.simpleName)

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val handlerMethod = handler as? HandlerMethod
        val methodAnnotation = handlerMethod?.getMethodAnnotation(AccessRole::class.java)
        val allowedRole = methodAnnotation?.allowedRole
        val userName = request.getAttribute(USER_NAME_ATR)?.toString()
        if (allowedRole != null && userName != null) {
            val userRole = userRoleCacheService.getUserRole(userName)
            if (userRole.priority > allowedRole.priority) {
                logger.info("User $userName not permitted to ${request.servletPath}")
                response.sendError(HttpServletResponse.SC_FORBIDDEN)
            } else {
                logger.info("User $userName grant access to ${request.servletPath}")
            }
        }
        return super.preHandle(request, response, handler)
    }
}