package com.example.jetbrains.security

import com.example.jetbrains.service.UserRoleCacheService
import org.slf4j.LoggerFactory
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthorizationInterceptor(private val userRoleCacheService: UserRoleCacheService) : HandlerInterceptor {
    private val logger = LoggerFactory.getLogger(AuthorizationInterceptor::class.simpleName)

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val handlerMethod = handler as? HandlerMethod
        val methodAnnotation = handlerMethod?.getMethodAnnotation(AccessRole::class.java)
        val permission = methodAnnotation?.permission
        val userName = request.getAttribute(USER_NAME_ATR)?.toString()
        if (permission != null && userName != null) {
            val userRole = userRoleCacheService.getUserRole(userName)
            if (permission.role == userRole.roleName || userRole.priority < permission.priority) {
                RequestContextHolder.currentRequestAttributes()
                    .setAttribute(AUTH_USER_CONTEXT, AuthUserData(userName, userRole), RequestAttributes.SCOPE_REQUEST)
                logger.info("User $userName grant access to ${request.servletPath}")
            } else {
                logger.info("User $userName not permitted to ${request.servletPath}")
                response.sendError(HttpServletResponse.SC_FORBIDDEN)
            }
        }
        return super.preHandle(request, response, handler)
    }
}