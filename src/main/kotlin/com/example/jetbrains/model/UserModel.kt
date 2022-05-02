package com.example.jetbrains.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("users")
data class UserModel(
    @Id
    @Column("id")
    val id: Long?,

    @Column("user_name")
    val userName: String,

    @Column("password")
    var password: String,

    @Column("role")
    val role: UserRole
)