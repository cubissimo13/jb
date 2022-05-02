package com.example.jetbrains.repository.config

import org.springframework.context.annotation.Configuration
import redis.embedded.RedisServer
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Configuration
class EmbeddedRedis {
    //TODO : встроенный редис для удобства демонстрации
    var redisServer: RedisServer = RedisServer(6379)

    @PostConstruct
    fun startRedis() {
        redisServer.start()
    }

    @PreDestroy
    fun stopRedis() {
        redisServer.stop()
    }
}