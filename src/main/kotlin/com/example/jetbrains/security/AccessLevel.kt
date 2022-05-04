package com.example.jetbrains.security

enum class AccessLevel(val priority: Int) {
    ADMIN(0),
    REVIEWER(1),
    USER(2)
}