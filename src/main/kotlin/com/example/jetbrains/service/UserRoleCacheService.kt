package com.example.jetbrains.service

import com.example.jetbrains.model.UserModel
import com.example.jetbrains.model.UserRoleModel

interface UserRoleCacheService {
    fun getUserRole(userName: String): UserRoleModel
    fun changeUserRole(user: UserModel, userRole: UserRoleModel)
}