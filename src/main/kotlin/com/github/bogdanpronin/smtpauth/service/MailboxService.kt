package com.github.bogdanpronin.smtpauth.service

import com.github.bogdanpronin.smtpauth.model.Mailbox
import com.github.bogdanpronin.smtpauth.repository.MailboxRepository
import org.springframework.stereotype.Service

import org.apache.commons.codec.binary.Base64
import java.security.MessageDigest
import java.security.SecureRandom
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class MailboxService(
    private val mailboxRepository: MailboxRepository
) {
    fun register(email: String, password: String, name: String): Mailbox {
    if (mailboxRepository.findByUsername(email) != null) {
        throw IllegalArgumentException("User with email $email already exists")
    }
        val hashedPassword = encodeSSHA512(password)
        val domain = "messenger-mail.ru"
        val localpart = email.substringBefore('@')
        val l1 = if (localpart.isNotEmpty()) localpart[0].toString() else "u"
        val l2 = if (localpart.length > 1) localpart[1].toString() else "n"
        val l3 = if (localpart.length > 2) localpart[2].toString() else "k"
        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm.ss"))
        val maildir = "$domain/$l1/$l2/$l3/$localpart-$timestamp/"

        val mailbox = Mailbox(
            username = email,
            password = hashedPassword,
            name = name,
            maildir = maildir,
            quota = 1024,
            passwordlastchange = LocalDateTime.now()
        )
        return mailboxRepository.save(mailbox)
    }




    fun findByUsername(username: String): Mailbox? {
        return mailboxRepository.findByUsername(username)
    }

    private fun encodeSSHA512(password: String): String {
        val salt = ByteArray(8)
        SecureRandom().nextBytes(salt)
        val digest = MessageDigest.getInstance("SHA-512")
        digest.update(password.toByteArray(Charsets.UTF_8))
        digest.update(salt)
        val hash = digest.digest()
        val combined = hash + salt
        return "{SSHA512}" + Base64.encodeBase64String(combined)
    }

    fun verifySSHA512(password: String, storedHash: String): Boolean {
        if (!storedHash.startsWith("{SSHA512}")) return false
        val decoded = Base64.decodeBase64(storedHash.removePrefix("{SSHA512}"))
        val hash = decoded.copyOfRange(0, 64) // SHA-512 hash (64 bytes)
        val salt = decoded.copyOfRange(64, decoded.size)
        val digest = MessageDigest.getInstance("SHA-512")
        digest.update(password.toByteArray(Charsets.UTF_8))
        digest.update(salt)
        val computedHash = digest.digest()
        return hash.contentEquals(computedHash)
    }
}