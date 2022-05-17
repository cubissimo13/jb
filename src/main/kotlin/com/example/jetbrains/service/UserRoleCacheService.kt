package com.example.jetbrains.service

import com.example.jetbrains.model.UserModel
import com.example.jetbrains.model.UserRoleModel
import com.example.jetbrains.model.enum.UserRole

interface UserRoleCacheService {
    fun getAllUserRole(): List<UserRole>
    fun getUserRole(userName: String): UserRoleModel
    fun actualize(user: UserModel)
}