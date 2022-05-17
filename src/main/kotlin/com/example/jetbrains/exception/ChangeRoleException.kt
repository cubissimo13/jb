package com.example.jetbrains.exception

class ChangeRoleException (
    override val message: String,
    val userName: String
) : RuntimeException()