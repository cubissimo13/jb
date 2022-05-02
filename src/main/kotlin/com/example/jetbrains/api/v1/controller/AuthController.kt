package com.example.jetbrains.api.v1.controller

import com.example.jetbrains.api.v1.payload.request.ChangeRequest
import com.example.jetbrains.api.v1.payload.request.LoginRequest
import com.example.jetbrains.api.v1.payload.request.RegisterRequest
import com.example.jetbrains.api.v1.payload.response.ChangeResponse
import com.example.jetbrains.api.v1.payload.response.JwtResponse
import com.example.jetbrains.api.v1.payload.response.RegisterResponse
import com.example.jetbrains.security.AUTH_HEADER_NAME
import com.example.jetbrains.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(val userService: UserService) {

    @PostMapping("/register")
    fun registerUser(@Valid @RequestBody registerRequest: RegisterRequest): RegisterResponse {
        return userService.registerUser(
            userName = registerRequest.userName,
            password = registerRequest.password,
            role = registerRequest.role
        )
    }

    @PostMapping("/login")
    fun authenticateUser(@Valid @RequestBody loginRequest: LoginRequest): JwtResponse {
        return userService.authenticate(
            userName = loginRequest.userName,
            password = loginRequest.password
        )
    }

    @PutMapping("/change-password")
    fun changePassword(
        @Valid @RequestBody changeRequest: ChangeRequest,
        @RequestHeader(AUTH_HEADER_NAME) authHeader: String
    ): ChangeResponse {
        return userService.changePassword(
            newPassword = changeRequest.newPassword,
            authHeader = authHeader
        )
    }
}