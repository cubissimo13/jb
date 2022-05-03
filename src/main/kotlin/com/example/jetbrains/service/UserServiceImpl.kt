package com.example.jetbrains.service

import com.example.jetbrains.api.v1.payload.response.ChangeResponse
import com.example.jetbrains.api.v1.payload.response.JwtResponse
import com.example.jetbrains.api.v1.payload.response.RegisterResponse
import com.example.jetbrains.exciption.WrongPasswordException
import com.example.jetbrains.exciption.WrongUserException
import com.example.jetbrains.model.ExpiredToken
import com.example.jetbrains.model.UserModel
import com.example.jetbrains.model.UserRole
import com.example.jetbrains.repository.BlackListRepository
import com.example.jetbrains.repository.UserRepository
import org.mindrot.jbcrypt.BCrypt
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val blackListRepository: BlackListRepository,
    private val userRepository: UserRepository,
    private val tokenService: TokenService
) : UserService {

    private val logger = LoggerFactory.getLogger(UserServiceImpl::class.simpleName)

    override fun registerUser(userName: String, password: String, role: UserRole): RegisterResponse {
        if (userRepository.existsByUserName(userName)) {
            throw WrongUserException("User already exist", userName)
        }
        val saltedHash = BCrypt.hashpw(password, BCrypt.gensalt())
        val userModel = UserModel(id = null, userName = userName, password = saltedHash, role = role)
        userRepository.save(userModel)
        logger.info("Successful registered user $userName")
        return RegisterResponse("Successful registered user $userName")
    }

    override fun changePassword(newPassword: String, authHeader: String): ChangeResponse {
        val jwtToken = tokenService.getJwtTokenFromHeader(authHeader)
        val userName = tokenService.getUserName(jwtToken)
        val user = userRepository.findUserModelByUserName(userName) ?: throw WrongUserException("Not found", userName)
        val saltedHash = BCrypt.hashpw(newPassword, BCrypt.gensalt())
        user.password = saltedHash
        userRepository.save(user)
        addOldTokenToBlackList(jwtToken)
        logger.info("Successful changed password for user $userName")
        return ChangeResponse("Successful changed password for user $userName")
    }

    override fun authenticate(userName: String, password: String): JwtResponse {
        val user = userRepository.findUserModelByUserName(userName)
        if (user != null && BCrypt.checkpw(password, user.password)) {
            val jwtToken = tokenService.generateJwtToken(user)
            logger.info("Successful login user $userName")
            return JwtResponse(
                userName = user.userName,
                jwtToken = jwtToken
            )
        } else {
            throw WrongPasswordException(message = "Given wrong password", userName = userName)
        }
    }

    private fun addOldTokenToBlackList(jwtToken: String) {
        val ttl = tokenService.getExpirationDate(jwtToken)
        blackListRepository.save(ExpiredToken(jwtToken, ttl))
        logger.info("Token added to blacklist with ttl $ttl")
    }
}