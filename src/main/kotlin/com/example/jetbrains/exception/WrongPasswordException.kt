package com.example.jetbrains.exception

class WrongPasswordException(
    override val message: String,
    val userName: String
) : RuntimeException(message)