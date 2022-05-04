package com.example.jetbrains.exciption

class UserExistException(
    override val message: String,
    val userName: String
) : RuntimeException()