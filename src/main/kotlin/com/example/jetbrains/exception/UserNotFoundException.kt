package com.example.jetbrains.exception

class UserNotFoundException(
    override val message: String,
    val userName: String
) : RuntimeException(message)