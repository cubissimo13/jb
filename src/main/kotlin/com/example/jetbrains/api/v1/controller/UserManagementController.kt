package com.example.jetbrains.api.v1.controller

import com.example.jetbrains.api.v1.payload.response.ChangeResponse
import com.example.jetbrains.api.v1.payload.response.UserResponse
import com.example.jetbrains.model.enum.UserRole
import com.example.jetbrains.security.AccessLevel
import com.example.jetbrains.security.AccessRole
import com.example.jetbrains.service.UserRoleCacheService
import com.example.jetbrains.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user-management")
class UserManagementController(
    private val userService: UserService,
    private val userRoleCacheService: UserRoleCacheService
) {

    @GetMapping("/users")
    @AccessRole(AccessLevel.ADMIN_ACCESS)
    fun getUsers(): ResponseEntity<List<UserResponse>> {
        val allUsers = userService.getAllUsers()
        return ResponseEntity.ok(allUsers)
    }

    @GetMapping("/user-roles")
    @AccessRole(AccessLevel.ADMIN_ACCESS)
    fun getUserRoles(): ResponseEntity<List<UserRole>> {
        val allUserRole = userRoleCacheService.getAllUserRole()
        return ResponseEntity.ok(allUserRole)
    }

    @PutMapping("/{userId}/change-role")
    @AccessRole(AccessLevel.ADMIN_ACCESS)
    fun changeUserRole(@PathVariable userId: Long, @RequestParam userRole: UserRole): ChangeResponse {
        return userService.changeRole(userId, userRole)
    }
}