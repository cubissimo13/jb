package com.example.jetbrains.api.v1.controller

import com.example.jetbrains.api.v1.payload.response.JwtResponse
import com.example.jetbrains.api.v1.payload.response.RegisterResponse
import com.example.jetbrains.model.enum.UserRole
import com.example.jetbrains.security.AUTH_HEADER_NAME
import com.example.jetbrains.security.AUTH_TOKEN_START
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@Execution(ExecutionMode.CONCURRENT)
class AuthControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun testSuccessRegistrationAdmin() {
        val userName = "1"
        val userPassword = "Password1!"

        //Registration
        val userId = registration(userName = userName, password = userPassword)

        //Sign in by builtin admin
        val superAdminToken = signIn("ROOT", "Password1!")

        //Change default user role
        changeRole(id = userId, newRole = UserRole.ADMIN, token = superAdminToken)

        //Signing by just registered user
        val userToken = signIn(userName = userName, password = userPassword)

        //Check resource access for registered user
        checkResourceAccess(token = userToken, userRole = UserRole.ADMIN, userName)

        //Change password for registered user
        changePassword(userToken)

        //Check Unauthorized after password change
        checkUnauthorized(userToken)
    }

    @Test
    fun testSuccessRegistrationReviewer() {
        val userName = "2"
        val userPassword = "Password2!"

        //Registration
        val userId = registration(userName = userName, password = userPassword)

        //Sign in by builtin admin
        val superAdminToken = signIn("ROOT", "Password1!")

        //Change default user role
        changeRole(id = userId, newRole = UserRole.REVIEWER, token = superAdminToken)

        //Signing by just registered user
        val userToken = signIn(userName = userName, password = userPassword)

        //Check resource access for registered user
        checkResourceAccess(token = userToken, userRole = UserRole.REVIEWER, userName)

        //Change password for registered user
        changePassword(userToken)

        //Check Unauthorized after password change
        checkUnauthorized(userToken)
    }

    @Test
    fun testSuccessRegistrationUser() {
        val userName = "3"
        val userPassword = "Password3!"

        //Registration
        registration(userName = userName, password = userPassword)

        //Signing by just registered user
        val userToken = signIn(userName = userName, password = userPassword)

        //Check resource access for registered user
        checkResourceAccess(token = userToken, userRole = UserRole.USER, userName)

        //Change password for registered user
        changePassword(userToken)

        //Check Unauthorized after password change
        checkUnauthorized(userToken)
    }

    private fun registration(userName: String, password: String): Long {
        val registerResponse = mockMvc.perform(
            post("/api/v1/auth/register")
                .servletPath("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\n" +
                        " \"userName\": \"$userName\",\n" +
                        " \"password\": \"$password\"\n" +
                        "}"
                )
        )
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        return objectMapper.readValue(registerResponse, RegisterResponse::class.java).id
    }

    private fun signIn(userName: String, password: String): String {
        val loginResponse = mockMvc.perform(
            post("/api/v1/auth/login")
                .servletPath("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\n" +
                        " \"userName\": \"$userName\",\n" +
                        " \"password\": \"$password\"\n" +
                        "}"
                )
        )
            .andExpect(status().isOk)
            .andReturn().response.contentAsString
        return objectMapper.readValue(loginResponse, JwtResponse::class.java).jwtToken
    }

    private fun changeRole(id: Long, newRole: UserRole, token: String) {
        mockMvc.perform(
            put("/api/v1/user-management/$id/change-role?userRole=${newRole.name}")
                .servletPath("/api/v1/user-management")
                .header(AUTH_HEADER_NAME, AUTH_TOKEN_START + token)
        ).andExpect(status().isOk)
    }

    private fun changePassword(token: String) {
        mockMvc.perform(
            put("/api/v1/auth/change-password")
                .servletPath("/api/v1/auth/change-password")
                .header(AUTH_HEADER_NAME, AUTH_TOKEN_START + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\n" +
                        " \"newPassword\": \"Password9!\"\n" +
                        "}"
                )
        ).andExpect(status().isOk)
    }

    private fun checkResourceAccess(token: String, userRole: UserRole, userName: String) {
        //Resource access admin
        val performAdminResource = mockMvc.perform(
            get("/api/v1/internal/admin")
                .servletPath("/api/v1/internal/admin")
                .header(AUTH_HEADER_NAME, AUTH_TOKEN_START + token)
        )
        //Resource access reviewer
        val performReviewerResource = mockMvc.perform(
            get("/api/v1/internal/reviewer")
                .servletPath("/api/v1/internal/reviewer")
                .header(AUTH_HEADER_NAME, AUTH_TOKEN_START + token)
        )
        //Resource access user
        val performUserResource = mockMvc.perform(
            get("/api/v1/internal/user")
                .servletPath("/api/v1/internal/user")
                .header(AUTH_HEADER_NAME, AUTH_TOKEN_START + token)
        )
        when (userRole) {
            UserRole.ADMIN -> {
                performAdminResource.andExpect(status().isOk)
                performAdminResource.andExpect(content()
                    .json("{\"message\" : \"There is admins resource! user - $userName ,with role - ${userRole.name}\"}"))

                performReviewerResource.andExpect(status().isOk)
                performReviewerResource.andExpect(content()
                    .json("{\"message\" : \"There is reviewers resource! user - $userName ,with role - ${userRole.name}\"}"))

                performUserResource.andExpect(status().isOk)
                performUserResource.andExpect(content()
                    .json("{\"message\" : \"There is users resource! user - $userName ,with role - ${userRole.name}\"}"))
            }
            UserRole.REVIEWER -> {
                performAdminResource.andExpect(status().isForbidden)

                performReviewerResource.andExpect(status().isOk)
                performReviewerResource.andExpect(content()
                    .json("{\"message\" : \"There is reviewers resource! user - $userName ,with role - ${userRole.name}\"}"))

                performUserResource.andExpect(status().isOk)
                performUserResource.andExpect(content()
                    .json("{\"message\" : \"There is users resource! user - $userName ,with role - ${userRole.name}\"}"))
            }
            UserRole.USER -> {
                performAdminResource.andExpect(status().isForbidden)

                performReviewerResource.andExpect(status().isForbidden)

                performUserResource.andExpect(status().isOk)
                performUserResource.andExpect(content()
                    .json("{\"message\" : \"There is users resource! user - $userName ,with role - ${userRole.name}\"}"))
            }
        }
    }

    private fun checkUnauthorized(token: String) {
        //Resource access admin
        mockMvc.perform(
            get("/api/v1/internal/admin")
                .servletPath("/api/v1/internal/admin")
                .header(AUTH_HEADER_NAME, AUTH_TOKEN_START + token)
        ).andExpect(status().isUnauthorized)

        //Resource access reviewer
        mockMvc.perform(
            get("/api/v1/internal/reviewer")
                .servletPath("/api/v1/internal/reviewer")
                .header(AUTH_HEADER_NAME, AUTH_TOKEN_START + token)
        ).andExpect(status().isUnauthorized)

        //Resource access user
        mockMvc.perform(
            get("/api/v1/internal/user")
                .servletPath("/api/v1/internal/user")
                .header(AUTH_HEADER_NAME, AUTH_TOKEN_START + token)
        ).andExpect(status().isUnauthorized)
    }
}