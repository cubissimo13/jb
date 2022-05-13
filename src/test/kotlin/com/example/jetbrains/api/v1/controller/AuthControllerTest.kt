package com.example.jetbrains.api.v1.controller

import com.example.jetbrains.api.v1.payload.response.JwtResponse
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
        //Registration
        mockMvc.perform(
            post("/api/v1/auth/register")
                .servletPath("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\n" +
                        " \"userName\": \"1\",\n" +
                        " \"password\": \"Password1!\",\n" +
                        " \"role\": \"ADMIN\"\n" +
                        "}"
                )
        ).andExpect(status().isOk)

        //Sign in
        val loginResponse = mockMvc.perform(
            post("/api/v1/auth/login")
                .servletPath("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\n" +
                        " \"userName\": \"1\",\n" +
                        " \"password\": \"Password1!\"\n" +
                        "}"
                )
        ).andExpect(status().isOk)
            .andReturn().response.contentAsString
        val jwtResponse = objectMapper.readValue(loginResponse, JwtResponse::class.java)

        //Resource access admin
        mockMvc.perform(
            get("/api/v1/internal/admin")
                .servletPath("/api/v1/internal/admin")
                .header(AUTH_HEADER_NAME, AUTH_TOKEN_START + jwtResponse.jwtToken)
        )
            .andExpect(status().isOk)
            .andExpect(content().json("{\"message\" : \"There is admins resource! user - 1 ,with role - ADMIN\"}"))

        //Resource access reviewer
        mockMvc.perform(
            get("/api/v1/internal/reviewer")
                .servletPath("/api/v1/internal/reviewer")
                .header(AUTH_HEADER_NAME, AUTH_TOKEN_START + jwtResponse.jwtToken)
        )
            .andExpect(status().isOk)
            .andExpect(content().json("{\"message\" : \"There is reviewers resource! user - 1 ,with role - ADMIN\"}"))

        //Resource access user
        mockMvc.perform(
            get("/api/v1/internal/user")
                .servletPath("/api/v1/internal/user")
                .header(AUTH_HEADER_NAME, AUTH_TOKEN_START + jwtResponse.jwtToken)
        )
            .andExpect(status().isOk)
            .andExpect(content().json("{\"message\" : \"There is users resource! user - 1 ,with role - ADMIN\"}"))

        //Change password
        mockMvc.perform(
            put("/api/v1/auth/change-password")
                .servletPath("/api/v1/auth/change-password")
                .header(AUTH_HEADER_NAME, AUTH_TOKEN_START + jwtResponse.jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\n" +
                        " \"newPassword\": \"Password2!\"\n" +
                        "}"
                )
        ).andExpect(status().isOk)

        //Resource access admin
        mockMvc.perform(
            get("/api/v1/internal/admin")
                .servletPath("/api/v1/internal/admin")
                .header(AUTH_HEADER_NAME, AUTH_TOKEN_START + jwtResponse.jwtToken)
        ).andExpect(status().isUnauthorized)

        //Resource access reviewer
        mockMvc.perform(
            get("/api/v1/internal/reviewer")
                .servletPath("/api/v1/internal/reviewer")
                .header(AUTH_HEADER_NAME, AUTH_TOKEN_START + jwtResponse.jwtToken)
        ).andExpect(status().isUnauthorized)

        //Resource access user
        mockMvc.perform(
            get("/api/v1/internal/user")
                .servletPath("/api/v1/internal/user")
                .header(AUTH_HEADER_NAME, AUTH_TOKEN_START + jwtResponse.jwtToken)
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun testSuccessRegistrationReviewer() {
        //Registration
        mockMvc.perform(
            post("/api/v1/auth/register")
                .servletPath("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\n" +
                        " \"userName\": \"2\",\n" +
                        " \"password\": \"Password1!\",\n" +
                        " \"role\": \"REVIEWER\"\n" +
                        "}"
                )
        ).andExpect(status().isOk)

        //Sign in
        val loginResponse = mockMvc.perform(
            post("/api/v1/auth/login")
                .servletPath("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\n" +
                        " \"userName\": \"2\",\n" +
                        " \"password\": \"Password1!\"\n" +
                        "}"
                )
        ).andExpect(status().isOk)
            .andReturn().response.contentAsString
        val jwtResponse = objectMapper.readValue(loginResponse, JwtResponse::class.java)

        //Resource access admin
        mockMvc.perform(
            get("/api/v1/internal/admin")
                .servletPath("/api/v1/internal/admin")
                .header(AUTH_HEADER_NAME, AUTH_TOKEN_START + jwtResponse.jwtToken)
        ).andExpect(status().isForbidden)

        //Resource access reviewer
        mockMvc.perform(
            get("/api/v1/internal/reviewer")
                .servletPath("/api/v1/internal/reviewer")
                .header(AUTH_HEADER_NAME, AUTH_TOKEN_START + jwtResponse.jwtToken)
        )
            .andExpect(status().isOk)
            .andExpect(content().json("{\"message\" : \"There is reviewers resource! user - 2 ,with role - REVIEWER\"}"))

        //Resource access user
        mockMvc.perform(
            get("/api/v1/internal/user")
                .servletPath("/api/v1/internal/user")
                .header(AUTH_HEADER_NAME, AUTH_TOKEN_START + jwtResponse.jwtToken)
        )
            .andExpect(status().isOk)
            .andExpect(content().json("{\"message\" : \"There is users resource! user - 2 ,with role - REVIEWER\"}"))
        //Change password
        mockMvc.perform(
            put("/api/v1/auth/change-password")
                .servletPath("/api/v1/auth/change-password")
                .header(AUTH_HEADER_NAME, AUTH_TOKEN_START + jwtResponse.jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\n" +
                        " \"newPassword\": \"Password2!\"\n" +
                        "}"
                )
        ).andExpect(status().isOk)

        //Resource access admin
        mockMvc.perform(
            get("/api/v1/internal/admin")
                .servletPath("/api/v1/internal/admin")
                .header(AUTH_HEADER_NAME, AUTH_TOKEN_START + jwtResponse.jwtToken)
        ).andExpect(status().isUnauthorized)

        //Resource access reviewer
        mockMvc.perform(
            get("/api/v1/internal/reviewer")
                .servletPath("/api/v1/internal/reviewer")
                .header(AUTH_HEADER_NAME, AUTH_TOKEN_START + jwtResponse.jwtToken)
        ).andExpect(status().isUnauthorized)

        //Resource access user
        mockMvc.perform(
            get("/api/v1/internal/user")
                .servletPath("/api/v1/internal/user")
                .header(AUTH_HEADER_NAME, AUTH_TOKEN_START + jwtResponse.jwtToken)
        ).andExpect(status().isUnauthorized)
    }

    @Test()
    fun testSuccessRegistrationUser() {
        //Registration
        mockMvc.perform(
            post("/api/v1/auth/register")
                .servletPath("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\n" +
                        " \"userName\": \"3\",\n" +
                        " \"password\": \"Password1!\",\n" +
                        " \"role\": \"USER\"\n" +
                        "}"
                )
        ).andExpect(status().isOk)

        //Sign in
        val loginResponse = mockMvc.perform(
            post("/api/v1/auth/login")
                .servletPath("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\n" +
                        " \"userName\": \"3\",\n" +
                        " \"password\": \"Password1!\"\n" +
                        "}"
                )
        ).andExpect(status().isOk)
            .andReturn().response.contentAsString
        val jwtResponse = objectMapper.readValue(loginResponse, JwtResponse::class.java)

        //Resource access admin
        mockMvc.perform(
            get("/api/v1/internal/admin")
                .servletPath("/api/v1/internal/admin")
                .header(AUTH_HEADER_NAME, AUTH_TOKEN_START + jwtResponse.jwtToken)
        ).andExpect(status().isForbidden)

        //Resource access reviewer
        mockMvc.perform(
            get("/api/v1/internal/reviewer")
                .servletPath("/api/v1/internal/reviewer")
                .header(AUTH_HEADER_NAME, AUTH_TOKEN_START + jwtResponse.jwtToken)
        ).andExpect(status().isForbidden)

        //Resource access user
        mockMvc.perform(
            get("/api/v1/internal/user")
                .servletPath("/api/v1/internal/user")
                .header(AUTH_HEADER_NAME, AUTH_TOKEN_START + jwtResponse.jwtToken)
        )
            .andExpect(status().isOk)
            .andExpect(content().json("{\"message\" : \"There is users resource! user - 3 ,with role - USER\"}"))

        //Change password
        mockMvc.perform(
            put("/api/v1/auth/change-password")
                .servletPath("/api/v1/auth/change-password")
                .header(AUTH_HEADER_NAME, AUTH_TOKEN_START + jwtResponse.jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\n" +
                        " \"newPassword\": \"Password2!\"\n" +
                        "}"
                )
        ).andExpect(status().isOk)

        //Resource access admin
        mockMvc.perform(
            get("/api/v1/internal/admin")
                .servletPath("/api/v1/internal/admin")
                .header(AUTH_HEADER_NAME, AUTH_TOKEN_START + jwtResponse.jwtToken)
        ).andExpect(status().isUnauthorized)

        //Resource access reviewer
        mockMvc.perform(
            get("/api/v1/internal/reviewer")
                .servletPath("/api/v1/internal/reviewer")
                .header(AUTH_HEADER_NAME, AUTH_TOKEN_START + jwtResponse.jwtToken)
        ).andExpect(status().isUnauthorized)

        //Resource access user
        mockMvc.perform(
            get("/api/v1/internal/user")
                .servletPath("/api/v1/internal/user")
                .header(AUTH_HEADER_NAME, AUTH_TOKEN_START + jwtResponse.jwtToken)
        ).andExpect(status().isUnauthorized)
    }
}