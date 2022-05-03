package com.example.jetbrains.repository.redis

import com.example.jetbrains.model.redis.UserRoleCache
import org.springframework.data.keyvalue.repository.KeyValueRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRoleCacheRepository: KeyValueRepository<UserRoleCache, String>