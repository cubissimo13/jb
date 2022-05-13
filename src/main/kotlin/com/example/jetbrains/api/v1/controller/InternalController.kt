package com.example.jetbrains.api.v1.controller

import com.example.jetbrains.api.v1.payload.response.ResourceResponse
import com.example.jetbrains.security.AccessLevel
import com.example.jetbrains.security.AccessRole
import com.example.jetbrains.service.AwesomeBusinessLogic
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/internal")
class InternalController(private val awesomeBusinessLogic: AwesomeBusinessLogic) {
    @GetMapping("/admin")
    @AccessRole(AccessLevel.ADMIN_ACCESS)
    fun getAdminResource(): ResourceResponse {
        return ResourceResponse("There is admins resource! ${awesomeBusinessLogic.getProfit()}")
    }

    @GetMapping("/reviewer")
    @AccessRole(AccessLevel.REVIEWER_ACCESS)
    fun getReviewerResource(): ResourceResponse {
        return ResourceResponse("There is reviewers resource! ${awesomeBusinessLogic.getProfit()}")
    }

    @GetMapping("/user")
    @AccessRole(AccessLevel.USER_ACCESS)
    fun getUserResource(): ResourceResponse {
        return ResourceResponse("There is users resource! ${awesomeBusinessLogic.getProfit()}")
    }
}