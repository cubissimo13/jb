package com.example.jetbrains.api.v1.payload.request

import com.example.jetbrains.validation.ValidPassword
import javax.validation.constraints.NotBlank

data class LoginRequest(
    @NotBlank
    val userName: String,

    @ValidPassword
    val password: String
)