package com.example.jetbrains.service

import com.example.jetbrains.api.v1.payload.response.ChangeResponse
import com.example.jetbrains.api.v1.payload.response.JwtResponse
import com.example.jetbrains.api.v1.payload.response.RegisterResponse
import com.example.jetbrains.api.v1.payload.response.UserResponse
import com.example.jetbrains.exception.ChangeRoleException
import com.example.jetbrains.exception.SamePasswordException
import com.example.jetbrains.exception.UserExistException
import com.example.jetbrains.exception.UserNotFoundException
import com.example.jetbrains.exception.WrongPasswordException
import com.example.jetbrains.model.UserModel
import com.example.jetbrains.model.enum.UserRole
import com.example.jetbrains.model.redis.ExpiredTokenCache
import com.example.jetbrains.repository.UserRepository
import com.example.jetbrains.repository.UserRoleRepository
import com.example.jetbrains.repository.redis.BlackListCacheRepository
import com.example.jetbrains.security.AUTH_USER_CONTEXT
import com.example.jetbrains.security.AuthUserData
import org.mindrot.jbcrypt.BCrypt
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder

@Service
class UserServiceImpl(
    private val blackListCacheRepository: BlackListCacheRepository,
    private val userRepository: UserRepository,
    private val userRoleRepository: UserRoleRepository,
    private val userRoleCacheService: UserRoleCacheService,
    private val tokenService: TokenService
) : UserService {

    private val logger = LoggerFactory.getLogger(UserServiceImpl::class.simpleName)
    override fun getAllUsers(): List<UserResponse> {
        return userRepository.getAllUsers()
            .map { UserResponse(id = it.id!!, userName = it.userName, role = it.role.roleName) }
            .toList()
    }

    @Transactional
    override fun registerUser(userName: String, password: String): RegisterResponse {
        if (userRepository.existByUserName(userName)) {
            throw UserExistException("User already exist", userName)
        }
        val saltedHash = BCrypt.hashpw(password, BCrypt.gensalt())
        val roleModel = userRoleRepository.findByRoleName(UserRole.USER.name) ?: throw IllegalStateException()
        val userModel = UserModel(id = null, userName = userName, password = saltedHash, role = roleModel)
        val id = userRepository.insertUser(userModel)
        logger.info("Successful registered user $userName")
        return RegisterResponse(id = id!!, userName = userName, "Successful registered")
    }

    @Transactional
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

    @Transactional
    override fun changeRole(userId: Long, userRole: UserRole): ChangeResponse {
        val authUserName = getAuthUser()
        val userModel = userRepository.findUserById(userId) ?: throw UserNotFoundException("Not found", userId.toString())
        if (authUserName == userModel.userName) throw ChangeRoleException("Cant change your own role", authUserName)
        val newRole = userRoleRepository.findByRoleName(userRole.name) ?: throw IllegalStateException("Role not exist")
        userModel.role = newRole
        userRepository.updateUser(userModel)
        userRoleCacheService.actualize(userModel)
        return ChangeResponse("Successful changed role for user ${userModel.userName}")
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

    private fun getAuthUser(): String? {
        val authUserData =
            RequestContextHolder.currentRequestAttributes()
                .getAttribute(AUTH_USER_CONTEXT, RequestAttributes.SCOPE_REQUEST) as? AuthUserData
        return authUserData?.authUserName
    }
}