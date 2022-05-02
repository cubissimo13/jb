package com.example.jetbrains.service

import com.example.jetbrains.api.v1.payload.response.ChangeResponse
import com.example.jetbrains.api.v1.payload.response.JwtResponse
import com.example.jetbrains.api.v1.payload.response.RegisterResponse
import com.example.jetbrains.model.UserRole

/**
 * Interface that provide methods for operation with user
 */
interface UserService {
    /**
     * User registration
     * @param userName - Login for authorization
     * @param password - Password for authorization
     * @param role     - Role for authentication
     * @return Result message
     */
    fun registerUser(userName: String, password: String, role: UserRole): RegisterResponse

    /**
     * Change password for registered user
     * @param newPassword - New password for authorization
     * @param authHeader  - Authorization header with valid token
     * @return Result message
     */
    fun changePassword(newPassword: String, authHeader: String): ChangeResponse

    /**
     * Receiving jwt token for registered user
     * @param userName - Login for authorization
     * @param password - Password for authorization
     * @return Result message with generated jwt token
     */
    fun authenticate(userName: String, password: String): JwtResponse
}