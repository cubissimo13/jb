package com.example.jetbrains.security

import com.example.jetbrains.model.UserRoleModel

data class AuthUserData(
    val authUserName: String,
    val authUserRole: UserRoleModel
)