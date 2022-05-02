package com.example.jetbrains.api.v1.payload.response

data class ErrorResponse(
    val status: Int,
    val message: String
)