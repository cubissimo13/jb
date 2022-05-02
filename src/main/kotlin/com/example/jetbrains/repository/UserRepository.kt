package com.example.jetbrains.repository

import com.example.jetbrains.model.UserModel
import com.example.jetbrains.model.UserRole
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<UserModel, Long> {
    fun existsByUserName(userName: String): Boolean
    fun findUserModelByUserName(userName: String): UserModel?
    @Query("select role from users where user_name = :userName")
    fun getRole(userName: String): UserRole
}