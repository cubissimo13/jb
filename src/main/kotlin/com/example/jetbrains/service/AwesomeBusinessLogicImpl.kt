package com.example.jetbrains.service

import com.example.jetbrains.security.AUTH_USER_CONTEXT
import com.example.jetbrains.security.AuthUserData
import org.springframework.stereotype.Service
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder

@Service
class AwesomeBusinessLogicImpl : AwesomeBusinessLogic {

    override fun getProfit(): String {
        val authUserData =
            RequestContextHolder.currentRequestAttributes()
                .getAttribute(AUTH_USER_CONTEXT, RequestAttributes.SCOPE_REQUEST) as? AuthUserData
        val userName = authUserData?.authUserName
        val role = authUserData?.authUserRole?.roleName?.name
        return "user - $userName ,with role - $role"
    }
}