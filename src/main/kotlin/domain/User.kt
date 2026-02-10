package com.zhdanon.domain

import kotlinx.datetime.LocalDateTime
import java.util.UUID

data class User(
    val id: UUID,
    val email: String,
    val passwordHash: String,
    val createdAt: LocalDateTime,
    val role: String
)