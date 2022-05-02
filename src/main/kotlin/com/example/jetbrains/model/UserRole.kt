package com.example.jetbrains.model

enum class UserRole(val priority: Int) {
    ADMIN(0),
    REVIEWER(1),
    USER(2)
}