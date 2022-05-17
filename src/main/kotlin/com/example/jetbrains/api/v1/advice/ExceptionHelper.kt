package com.example.jetbrains.api.v1.advice

import com.example.jetbrains.api.v1.payload.response.ErrorResponse
import com.example.jetbrains.exception.ChangeRoleException
import com.example.jetbrains.exception.SamePasswordException
import com.example.jetbrains.exception.UserExistException
import com.example.jetbrains.exception.UserNotFoundException
import com.example.jetbrains.exception.WrongPasswordException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHelper {
    private val logger = LoggerFactory.getLogger(ExceptionHelper::class.simpleName)

    @ExceptionHandler(value = [WrongPasswordException::class])
    fun unauthorizedHandler(exception: WrongPasswordException): ResponseEntity<ErrorResponse> {
        logger.error("${exception.message} ${exception.userName}")
        return ResponseEntity(
            ErrorResponse(status = HttpStatus.UNAUTHORIZED.value(), error = "Wrong credential"), HttpStatus.UNAUTHORIZED
        )
    }

    @ExceptionHandler(value = [SamePasswordException::class])
    fun unauthorizedHandler(exception: SamePasswordException): ResponseEntity<ErrorResponse> {
        logger.error("${exception.message} ${exception.userName}")
        return ResponseEntity(
            ErrorResponse(status = HttpStatus.BAD_REQUEST.value(), error = "Same password"), HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(value = [UserNotFoundException::class])
    fun unauthorizedHandler(exception: UserNotFoundException): ResponseEntity<ErrorResponse> {
        logger.error("${exception.message} ${exception.userName}")
        return ResponseEntity(
            ErrorResponse(status = HttpStatus.BAD_REQUEST.value(), error = "User not found"), HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(value = [UserExistException::class])
    fun unauthorizedHandler(exception: UserExistException): ResponseEntity<ErrorResponse> {
        logger.error("${exception.message} ${exception.userName}")
        return ResponseEntity(
            ErrorResponse(status = HttpStatus.BAD_REQUEST.value(), error = "User already exist"), HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(value = [ChangeRoleException::class])
    fun unauthorizedHandler(exception: ChangeRoleException): ResponseEntity<ErrorResponse> {
        logger.error("${exception.message} ${exception.userName}")
        return ResponseEntity(
            ErrorResponse(status = HttpStatus.BAD_REQUEST.value(), error = "Cant change your own role"), HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(value = [RuntimeException::class])
    fun allExceptionHandler(exception: RuntimeException): ResponseEntity<ErrorResponse> {
        logger.error(exception.stackTraceToString())
        return ResponseEntity(
            ErrorResponse(status = HttpStatus.INTERNAL_SERVER_ERROR.value(), error = "Ooops! something wrong"),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }
}