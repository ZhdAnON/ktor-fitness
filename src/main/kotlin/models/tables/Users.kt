package com.zhdanon.models.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object Users : Table("users") {
    val id = uuid("id")
    val email = varchar("email", 255).uniqueIndex()
    val passwordHash = varchar("password_hash", 255)
    val createdAt = datetime("created_at") // kotlinx.datetime.LocalDateTime
    val role = varchar("role", 50).default("USER")
    val refreshToken = varchar("refresh_token", 512).nullable()
    val refreshExpiresAt = datetime("refresh_expires_at").nullable()

    override val primaryKey = PrimaryKey(id)
}