package com.example.jetbrains.exciption

class WrongUserException(
    override val message: String,
    val userName: String
) : RuntimeException(message)