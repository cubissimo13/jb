package com.example.jetbrains.validation

import org.slf4j.LoggerFactory
import java.util.regex.Pattern
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class PasswordValidator: ConstraintValidator<ValidPassword, String> {
    private val logger = LoggerFactory.getLogger(PasswordValidator::class.simpleName)
    private val pattern: Pattern =
        Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~\$^+=<>]).{8,20}\$")

    override fun isValid(password: String, context: ConstraintValidatorContext): Boolean {
        val result = pattern.matcher(password).matches()
        if (!result) logger.info("Password not valid")
        return result
    }
}