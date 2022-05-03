package com.example.jetbrains.repository.redis

import com.example.jetbrains.model.redis.ExpiredTokenCache
import org.springframework.data.keyvalue.repository.KeyValueRepository
import org.springframework.stereotype.Repository

@Repository
interface BlackListCacheRepository : KeyValueRepository<ExpiredTokenCache, String>
