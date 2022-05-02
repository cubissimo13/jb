package com.example.jetbrains.repository

import com.example.jetbrains.model.ExpiredToken
import org.springframework.data.keyvalue.repository.KeyValueRepository
import org.springframework.stereotype.Repository

@Repository
interface BlackListRepository : KeyValueRepository<ExpiredToken, String>
