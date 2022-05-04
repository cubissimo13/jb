package com.example.jetbrains.service

import com.example.jetbrains.api.v1.payload.response.ChangeResponse
import com.example.jetbrains.api.v1.payload.response.JwtResponse
import com.example.jetbrains.api.v1.payload.response.RegisterResponse
import com.example.jetbrains.exciption.SamePasswordException
import com.example.jetbrains.exciption.UserExistException
import com.example.jetbrains.exciption.UserNotFoundException
import com.example.jetbrains.exciption.WrongPasswordException
import com.example.jetbrains.model.UserModel
import com.example.jetbrains.model.redis.ExpiredTokenCache
import com.example.jetbrains.repository.UserRepository
import com.example.jetbrains.repository.UserRoleRepository
import com.example.jetbrains.repository.redis.BlackListCacheRepository
import org.mindrot.jbcrypt.BCrypt
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val blackListCacheRepository: BlackListCacheRepository,
    private val userRepository: UserRepository,
    private val userRoleRepository: UserRoleRepository,
    private val tokenService: TokenService
) : UserService {

    private val logger = LoggerFactory.getLogger(UserServiceImpl::class.simpleName)

    override fun registerUser(userName: String, password: String, role: String): RegisterResponse {
        if (userRepository.existByUserName(userName)) {
            throw UserExistException("User already exist", userName)
        }
        val saltedHash = BCrypt.hashpw(password, BCrypt.gensalt())
        val roleModel = userRoleRepository.findByRoleName(role) ?: throw IllegalStateException()
        val userModel = UserModel(id = null, userName = userName, password = saltedHash, role = roleModel)
        userRepository.insertUser(userModel)
        logger.info("Successful registered user $userName")
        return RegisterResponse("Successful registered user $userName")
    }

    override fun changePassword(newPassword: String, authHeader: String): ChangeResponse {
        val jwtToken = tokenService.getJwtTokenFromHeader(authHeader)
        val userName = tokenService.getUserName(jwtToken)
        val user = userRepository.findUserByName(userName) ?: throw UserNotFoundException("Not found", userName)
        if (BCrypt.checkpw(newPassword, user.password)) throw SamePasswordException("Same password for user", userName)
        val saltedHash = BCrypt.hashpw(newPassword, BCrypt.gensalt())
        user.password = saltedHash
        userRepository.updateUser(user)
        addOldTokenToBlackList(jwtToken)
        logger.info("Successful changed password for user $userName")
        return ChangeResponse("Successful changed password for user $userName")
    }

    override fun authenticate(userName: String, password: String): JwtResponse {
        val user = userRepository.findUserByName(userName) ?: throw UserNotFoundException("Not found", userName)
        if (BCrypt.checkpw(password, user.password)) {
            val jwtToken = tokenService.generateJwtToken(user)
            logger.info("Successful login user $userName")
            return JwtResponse(
                userName = user.userName,
                jwtToken = jwtToken
            )
        } else {
            throw WrongPasswordException(message = "Wrong credential", userName = userName)
        }
    }

    private fun addOldTokenToBlackList(jwtToken: String) {
        val ttl = tokenService.getExpirationDate(jwtToken)
        blackListCacheRepository.save(ExpiredTokenCache(jwtToken, ttl))
        logger.info("Token added to blacklist with ttl $ttl")
    }
}