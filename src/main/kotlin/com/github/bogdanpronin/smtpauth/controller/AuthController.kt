package com.github.bogdanpronin.smtpauth.controller

import com.github.bogdanpronin.smtpauth.dto.RegisterRequest
import com.github.bogdanpronin.smtpauth.service.MailboxService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val mailboxService: MailboxService
) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<Map<String, String>> {
        return try {
            mailboxService.register(request.email, request.password, request.name)
            ResponseEntity.ok(mapOf("message" to "User registered successfully"))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("message" to "Registration failed: ${e.message}"))
        }
    }

    @PostMapping("/login")
    fun login(principal: java.security.Principal): ResponseEntity<Map<String, String>> {
        val a = principal.name
        val mailbox = mailboxService.findByUsername(a)
            ?: return ResponseEntity.badRequest().body(mapOf("message" to "User not found", "name" to "null"))
        return ResponseEntity.ok(mapOf("message" to "Login successful", "name" to mailbox.name))
    }
}