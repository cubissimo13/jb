package com.example.jetbrains.exciption

class WrongPasswordException(
    override val message: String,
    val userName: String
) : RuntimeException(message)