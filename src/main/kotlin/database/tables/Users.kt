package com.zhdanon.database.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object Users : Table("users") {
    val id = uuid("id")
    val email = varchar("email", 255).uniqueIndex()
    val passwordHash = varchar("password_hash", 255)
    val createdAt = datetime("created_at") // kotlinx.datetime.LocalDateTime
    val role = varchar("role", 50).default("USER")

    override val primaryKey = PrimaryKey(id)
}