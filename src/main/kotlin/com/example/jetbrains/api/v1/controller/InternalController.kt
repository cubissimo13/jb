package com.example.jetbrains.api.v1.controller

import com.example.jetbrains.api.v1.payload.response.ResourceResponse
import com.example.jetbrains.model.UserRole
import com.example.jetbrains.security.AccessRole
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/internal")
class InternalController {
    @GetMapping("/admin")
    @AccessRole(UserRole.ADMIN)
    fun getAdminResource(): ResourceResponse {
        return ResourceResponse("There is admins resource")
    }

    @GetMapping("/reviewer")
    @AccessRole(UserRole.REVIEWER)
    fun getReviewerResource(): ResourceResponse {
        return ResourceResponse("There is reviewers resource")
    }

    @GetMapping("/user")
    @AccessRole(UserRole.USER)
    fun getUserResource(): ResourceResponse {
        return ResourceResponse("There is users resource")
    }
}