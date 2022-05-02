package com.example.jetbrains.security

import com.example.jetbrains.model.UserRole

@Target(AnnotationTarget.FUNCTION)
annotation class AccessRole(val allowedRole: UserRole)
