package com.example.jetbrains.model

data class UserModel(
    val id: Long?,
    val userName: String,
    var password: String,
    val role :UserRoleModel
)