package com.example.jetbrains.api.v1.payload.response

data class JwtResponse(
    val userId: Long,
    val userName: String,
    val jwtToken: String
)