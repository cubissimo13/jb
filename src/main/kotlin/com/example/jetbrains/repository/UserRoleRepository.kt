package com.example.jetbrains.repository

import com.example.jetbrains.model.UserRoleModel
import com.example.jetbrains.model.enum.UserRole
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class UserRoleRepository(private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {

    private val findRole = "select * from user_role where role_name = :name"

    fun findByRoleName(roleName: String): UserRoleModel? {
        val parameters = MapSqlParameterSource()
            .addValue("name", roleName)
        return try {
            namedParameterJdbcTemplate.queryForObject(findRole, parameters, RoleMapper())
        } catch (e: EmptyResultDataAccessException) {
            return null
        }
    }

    class RoleMapper: RowMapper<UserRoleModel> {
        override fun mapRow(rs: ResultSet, rowNum: Int): UserRoleModel {
            return UserRoleModel(
                id = rs.getLong("id"),
                roleName = UserRole.valueOf(rs.getString("role_name")),
                priority = rs.getInt("priority")
            )
        }
    }
}