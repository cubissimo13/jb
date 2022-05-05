package com.example.jetbrains.api.v1.payload.response

import java.time.Instant

data class ErrorResponse(
    val timestamp: Instant = Instant.now(),
    val status: Int,
    val error: String
)