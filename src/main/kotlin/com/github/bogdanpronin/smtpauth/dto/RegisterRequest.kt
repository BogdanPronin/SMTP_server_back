package com.github.bogdanpronin.smtpauth.dto

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String = ""
)