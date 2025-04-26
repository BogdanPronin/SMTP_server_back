package com.github.bogdanpronin.smtpauth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SmtpauthApplication

fun main(args: Array<String>) {
	runApplication<SmtpauthApplication>(*args)
}
