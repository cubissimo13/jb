package com.example.jetbrains.service

import com.example.jetbrains.model.UserModel
import com.example.jetbrains.repository.BlackListRepository
import com.example.jetbrains.security.AUTH_TOKEN_START
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Date

@Service
class TokenServiceImpl(private val blackListRepository: BlackListRepository) : TokenService {

    @Value("\${jetbrains.app.jwt.secret}")
    lateinit var jwtSecret: String

    @Value("\${jetbrains.app.jwt.expiration}")
    lateinit var jwtExpiration: String

    override fun generateJwtToken(userModel: UserModel): String {
        val currentDate = Date()
        return Jwts.builder()
            .setSubject(userModel.userName)
            .setIssuedAt(currentDate)
            .setExpiration(Date(currentDate.time + jwtExpiration.toLong()))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact()
    }

    override fun getJwtTokenFromHeader(authHeader: String): String {
        return authHeader.removePrefix(AUTH_TOKEN_START)
    }

    override fun validateToken(jwtToken: String): Boolean {
        return try {
            val parseClaimsJwt = parseClaims(jwtToken)
            (parseClaimsJwt.body.expiration > Date() && !blackListRepository.existsById(jwtToken))
        } catch (e: JwtException) {
            false
        }
    }

    override fun getUserName(jwtToken: String): String {
        return parseClaims(jwtToken).body.subject
    }

    override fun getExpirationDate(jwtToken: String): Long {
        val parseClaimsJwt = parseClaims(jwtToken)
        return parseClaimsJwt.body.expiration.time - Date().time
    }

    private fun parseClaims(jwtToken: String): Jws<Claims> {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken)
    }
}