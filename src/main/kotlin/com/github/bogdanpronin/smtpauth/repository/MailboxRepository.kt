package com.github.bogdanpronin.smtpauth.repository


import com.github.bogdanpronin.smtpauth.model.Mailbox
import org.springframework.data.jpa.repository.JpaRepository

interface MailboxRepository : JpaRepository<Mailbox, String> {
    fun findByUsername(username: String): Mailbox?
}