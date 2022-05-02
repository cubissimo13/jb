package com.example.jetbrains.security

import com.example.jetbrains.repository.UserRepository
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationInterceptor(private val userRepository: UserRepository) : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val handlerMethod = handler as? HandlerMethod
        val methodAnnotation = handlerMethod?.getMethodAnnotation(AccessRole::class.java)
        val allowedRole = methodAnnotation?.allowedRole
        val userName = request.getAttribute(USER_NAME_ATR)?.toString()
        if (allowedRole != null && userName != null) {
            val userRole = userRepository.getRole(userName)
            if (userRole.priority > allowedRole.priority) response.sendError(HttpServletResponse.SC_FORBIDDEN)
        }
        return super.preHandle(request, response, handler)
    }
}