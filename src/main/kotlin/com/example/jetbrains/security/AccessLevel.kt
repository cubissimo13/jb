package com.example.jetbrains.security

import com.example.jetbrains.model.enum.UserRole

enum class AccessLevel(val priority: Int, val role: UserRole) {
    ADMIN_ACCESS(0, UserRole.ADMIN),
    REVIEWER_ACCESS(1, UserRole.REVIEWER),
    USER_ACCESS(2, UserRole.USER)
}