package com.example.jetbrains.model

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import java.util.concurrent.TimeUnit

@RedisHash("Expired")
data class ExpiredToken(
    @Id
    val token: String,
    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    val ttl: Long
)