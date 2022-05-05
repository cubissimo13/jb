package com.example.jetbrains.exception

class SamePasswordException(
    override val message: String,
    val userName: String
) : RuntimeException()