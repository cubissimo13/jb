package com.example.jetbrains.model

data class UserModel(
    val id: Long?,
    val userName: String,
    var password: String,
    var role: UserRoleModel
)