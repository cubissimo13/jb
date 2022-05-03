package com.example.jetbrains.model.redis

import com.example.jetbrains.model.UserRole
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import java.util.concurrent.TimeUnit

@RedisHash("UserRoleCache")
data class UserRoleCache(
    @Id
    val userName: String,
    val userRole: UserRole,
    @TimeToLive(unit = TimeUnit.MINUTES)
    val ttl: Long
)