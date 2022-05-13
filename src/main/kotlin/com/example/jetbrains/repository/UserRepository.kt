package com.example.jetbrains.repository

import com.example.jetbrains.model.UserModel
import com.example.jetbrains.model.UserRoleModel
import com.example.jetbrains.model.enum.UserRole
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class UserRepository(private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {

    private val insertSql = "insert into users(user_name, password, role_id) values (:name, :password, :roleId)"

    private val updateSql = "update users" +
        " set user_name = :name, password = :password, role_id = :roleId" +
        " where id = :id"

    private val findUser = "select" +
        " users.id as userid," +
        " users.user_name," +
        " users.password," +
        " users.role_id," +
        " user_role.id as roleid," +
        " user_role.role_name," +
        " user_role.priority" +
        " from users join user_role on users.role_id = user_role.id" +
        " where user_name = :name"

    fun insertUser(userModel: UserModel) {
        val parameters = MapSqlParameterSource()
            .addValue("name", userModel.userName)
            .addValue("password", userModel.password)
            .addValue("roleId", userModel.role.id)
        namedParameterJdbcTemplate.update(insertSql, parameters)
    }

    fun updateUser(userModel: UserModel) {
        val parameters = MapSqlParameterSource()
            .addValue("id", userModel.id)
            .addValue("name", userModel.userName)
            .addValue("password", userModel.password)
            .addValue("roleId", userModel.role.id)
        namedParameterJdbcTemplate.update(updateSql, parameters)
    }

    fun findUserByName(userName: String): UserModel? {
        val parameters = MapSqlParameterSource()
            .addValue("name", userName)
        return try {
            namedParameterJdbcTemplate.queryForObject(findUser, parameters, UserMapper())
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }

    fun existByUserName(userName: String): Boolean {
        return findUserByName(userName) != null
    }

    class UserMapper : RowMapper<UserModel> {
        override fun mapRow(rs: ResultSet, rowNum: Int): UserModel {
            val userRoleModel = UserRoleModel(
                id = rs.getLong("roleid"),
                roleName = UserRole.valueOf(rs.getString("role_name")),
                priority = rs.getInt("priority")
            )
            return UserModel(
                id = rs.getLong("userid"),
                userName = rs.getString("user_name"),
                password = rs.getString("password"),
                role = userRoleModel
            )
        }
    }
}