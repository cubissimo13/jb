package com.example.jetbrains.api.v1.payload.response

data class RegisterResponse(
    val id: Long,
    val userName: String,
    val message: String
)