package com.example.jetbrains.model.redis

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import java.util.concurrent.TimeUnit

@RedisHash("Expired")
data class ExpiredTokenCache(
    @Id
    val token: String,
    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    val ttl: Long
)