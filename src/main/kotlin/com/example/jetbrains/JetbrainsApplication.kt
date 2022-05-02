package com.example.jetbrains

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JetbrainsApplication

fun main(args: Array<String>) {
	runApplication<JetbrainsApplication>(*args)
}
