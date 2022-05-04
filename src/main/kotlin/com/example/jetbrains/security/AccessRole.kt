package com.example.jetbrains.security

@Target(AnnotationTarget.FUNCTION)
annotation class AccessRole(val allowedRole: AccessLevel)
