package com.example.jetbrains.service

import com.example.jetbrains.model.UserModel
import com.example.jetbrains.model.UserRoleModel
import com.example.jetbrains.model.enum.UserRole
import com.example.jetbrains.model.redis.UserRoleCache
import com.example.jetbrains.repository.UserRepository
import com.example.jetbrains.repository.UserRoleRepository
import com.example.jetbrains.repository.redis.UserRoleCacheRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class UserRoleCacheServiceImpl(
    private val userRoleCacheRepository: UserRoleCacheRepository,
    private val userRoleRepository: UserRoleRepository,
    private val userRepository: UserRepository
) : UserRoleCacheService {

    @Value("\${jetbrains.app.userRole.cache.ttl.min}")
    lateinit var cacheTtl: String

    override fun getAllUserRole(): List<UserRole> {
        return userRoleRepository.getAllRole().map { it.roleName }.toList()
    }

    override fun getUserRole(userName: String): UserRoleModel {
        val cachedUserRole = userRoleCacheRepository.findById(userName)
        return if (cachedUserRole.isPresent) {
            cachedUserRole.get().userRole
        } else {
            val role = userRepository.findUserByName(userName)?.role ?: throw IllegalStateException()
            userRoleCacheRepository.save(UserRoleCache(userName, role, cacheTtl.toLong()))
            role
        }
    }

    override fun actualize(user: UserModel) {
        userRoleCacheRepository.save(UserRoleCache(user.userName, user.role, cacheTtl.toLong()))
    }
}