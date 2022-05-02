package com.example.jetbrains.api.v1.payload.request

import com.example.jetbrains.validation.ValidPassword

data class ChangeRequest(
    @ValidPassword
    val newPassword: String
)