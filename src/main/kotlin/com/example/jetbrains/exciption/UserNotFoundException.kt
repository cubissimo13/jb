package com.example.jetbrains.exciption

class UserNotFoundException(
    override val message: String,
    val userName: String
) : RuntimeException(message)