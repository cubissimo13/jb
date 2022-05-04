package com.example.jetbrains.exciption

class SamePasswordException(
    override val message: String,
    val userName: String
) : RuntimeException()