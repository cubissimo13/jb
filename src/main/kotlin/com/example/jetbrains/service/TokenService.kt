package com.example.jetbrains.service

import com.example.jetbrains.model.UserModel

/**
 * Interface that provide methods for operation with JWT token
 */
interface TokenService {
    /**
     * Generate jwt for registered user
     * @param userModel - Registered user
     * @return JWT token
     */
    fun generateJwtToken(userModel: UserModel): String

    /**
     * Extract jwt token from request header
     * @param authHeader - Authorization header with valid token
     * @return JWT token
     */
    fun getJwtTokenFromHeader(authHeader: String): String

    /**
     * Jwt token validation
     * @param jwtToken - jwt token
     * @return Result of validation
     */
    fun validateToken(jwtToken: String): Boolean

    /**
     * Extract username from jwt token
     * @param jwtToken - jwt token
     * @return Username
     */
    fun getUserName(jwtToken: String): String

    /**
     * Extract expiration date from jwt token
     * @param jwtToken - jwt token
     * @return Expiration date
     */
    fun getExpirationDate(jwtToken: String): Long
}