package com.github.bogdanpronin.smtpauth.dto

data class LoginRequest(
    val email: String,
    val password: String
)