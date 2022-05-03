package com.example.jetbrains.service

import com.example.jetbrains.model.UserModel
import com.example.jetbrains.model.UserRole

interface UserRoleCacheService {
    fun getUserRole(userName: String): UserRole
    fun changeUserRole(user: UserModel, userRole: UserRole)
}