package com.example.jetbrains.model

import com.example.jetbrains.model.enum.UserRole

data class UserRoleModel(
    var id: Long?,
    val roleName: UserRole,
    val priority: Int
)