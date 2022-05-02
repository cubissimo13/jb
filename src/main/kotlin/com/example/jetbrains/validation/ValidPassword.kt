package com.example.jetbrains.validation

import javax.validation.Constraint
import kotlin.reflect.KClass

@Constraint(validatedBy = [PasswordValidator::class])
@Target(AnnotationTarget.FIELD)
annotation class ValidPassword(
    val message: String = "Password should be at least 8 symbol with upper case and specific",
    val groups: Array<KClass<out Any>> = [],
    val payload: Array<KClass<out Any>> = []
)
