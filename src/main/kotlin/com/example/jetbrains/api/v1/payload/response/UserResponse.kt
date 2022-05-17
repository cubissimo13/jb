package com.example.jetbrains.api.v1.payload.response

import com.example.jetbrains.model.enum.UserRole

data class UserResponse(
    val id: Long,
    val userName: String,
    val role: UserRole
)