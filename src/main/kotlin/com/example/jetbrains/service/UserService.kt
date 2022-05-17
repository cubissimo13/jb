package com.example.jetbrains.service

import com.example.jetbrains.api.v1.payload.response.ChangeResponse
import com.example.jetbrains.api.v1.payload.response.JwtResponse
import com.example.jetbrains.api.v1.payload.response.RegisterResponse
import com.example.jetbrains.api.v1.payload.response.UserResponse
import com.example.jetbrains.model.enum.UserRole

/**
 * Interface that provide methods for operation with user
 */
interface UserService {
    /**
     * Get all  registered users
     * @return List of registered users
     */
    fun getAllUsers(): List<UserResponse>

    /**
     * User registration
     * @param userName - Login for authorization
     * @param password - Password for authorization
     * @return Result message
     */
    fun registerUser(userName: String, password: String): RegisterResponse

    /**
     * Change password for registered user
     * @param newPassword - New password for authorization
     * @param authHeader  - Authorization header with valid token
     * @return Result message
     */
    fun changePassword(newPassword: String, authHeader: String): ChangeResponse

    /**
     * Change role for registered user
     * @param userId - User ID
     * @param userRole  - new user role
     * @return Result message
     */
    fun changeRole(userId: Long, userRole: UserRole): ChangeResponse

    /**
     * Receiving jwt token for registered user
     * @param userName - Login for authorization
     * @param password - Password for authorization
     * @return Result message with generated jwt token
     */
    fun authenticate(userName: String, password: String): JwtResponse
}