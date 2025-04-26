package com.github.bogdanpronin.smtpauth.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.time.LocalDate

@Entity
@Table(name = "mailbox", schema = "public")
data class Mailbox(
    @Id
    val username: String,
    val password: String,
    val name: String = "",
    val language: String = "",
    @Column(name = "first_name")
    val firstName: String = "",
    @Column(name = "last_name")
    val lastName: String = "",
    val mobile: String = "",
    val telephone: String = "",
    @Column(name = "recovery_email")
    val recoveryEmail: String = "",
    val birthday: LocalDate = LocalDate.of(1, 1, 1),
    val mailboxformat: String = "maildir",
    val mailboxfolder: String = "Maildir",
    val storagebasedirectory: String = "/var/vmail",
    val storagenode: String = "vmail1",
    val maildir: String = "",
    val quota: Long = 1024,
    val domain: String = "messenger-mail.ru",
    val transport: String = "",
    val department: String = "",
    val rank: String = "normal",
    val employeeid: String? = "",
    val isadmin: Short = 0,
    val isglobaladmin: Short = 0,
    val enablesmtp: Short = 1,
    val enablesmtpsecured: Short = 1,
    val enablepop3: Short = 1,
    val enablepop3secured: Short = 1,
    val enablepop3tls: Short = 1,
    val enableimap: Short = 1,
    val enableimapsecured: Short = 1,
    val enableimaptls: Short = 1,
    val enabledeliver: Short = 1,
    val enablelda: Short = 1,
    val enablemanagesieve: Short = 1,
    val enablemanagesievesecured: Short = 1,
    val enablesieve: Short = 1,
    val enablesievesecured: Short = 1,
    val enablesievetls: Short = 1,
    val enableinternal: Short = 1,
    val enabledoveadm: Short = 1,
    @Column(name = "enablelib-storage")
    val enableLibStorage: Short = 1,
    @Column(name = "enablequota-status")
    val enableQuotaStatus: Short = 1,
    @Column(name = "enableindexer-worker")
    val enableIndexerWorker: Short = 1,
    val enablelmtp: Short = 1,
    val enabledsync: Short = 1,
    val enablesogo: Short = 1,
    val enablesogowebmail: String = "y",
    val enablesogocalendar: String = "y",
    val enablesogoactivesync: String = "y",
    @Column(name = "allow_nets")
    val allowNets: String? = null,
    val disclaimer: String = "",
    val settings: String = "",
    val passwordlastchange: LocalDateTime = LocalDateTime.now(),
    val created: LocalDateTime = LocalDateTime.now(),
    val modified: LocalDateTime = LocalDateTime.now(),
    val expired: LocalDateTime = LocalDateTime.parse("9999-12-31T01:01:01"),
    val active: Short = 1
)