package com.example.jetbrains.api.v1.payload.request

import com.example.jetbrains.model.UserRole
import com.example.jetbrains.validation.ValidPassword
import javax.validation.constraints.NotBlank

data class RegisterRequest(
    @NotBlank
    val userName: String,

    @ValidPassword
    val password: String,

    @NotBlank
    val role: UserRole
)