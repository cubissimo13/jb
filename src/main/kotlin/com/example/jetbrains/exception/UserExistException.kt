package com.example.jetbrains.exception

class UserExistException(
    override val message: String,
    val userName: String
) : RuntimeException()