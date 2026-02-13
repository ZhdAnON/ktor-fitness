package com.zhdanon.repository

import com.zhdanon.models.tables.Users
import com.zhdanon.models.domain.User
import kotlinx.datetime.Clock
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class UserRepository {

    fun createUser(email: String, passwordHash: String, role: String = "USER"): User {
        val id = UUID.randomUUID()
        val now = Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC)

        transaction {
            Users.insert {
                it[Users.id] = id
                it[Users.email] = email
                it[Users.passwordHash] = passwordHash
                it[Users.createdAt] = now   // ВАЖНО: теперь тип совпадает!
                it[Users.role] = role
            }
        }

        return User(
            id = id,
            email = email,
            passwordHash = passwordHash,
            createdAt = now,
            role = role
        )
    }

    fun findByEmail(email: String): User? = transaction {
        Users.selectAll().where { Users.email eq email }
            .map(::rowToUser)
            .singleOrNull()
    }

    fun findById(id: UUID): User? = transaction {
        Users.selectAll().where { Users.id eq id }
            .map(::rowToUser)
            .singleOrNull()
    }

    fun emailExists(email: String): Boolean = transaction {
        Users.selectAll().where { Users.email eq email }.count() > 0
    }

    private fun rowToUser(row: ResultRow): User {
        return User(
            id = row[Users.id],
            email = row[Users.email],
            passwordHash = row[Users.passwordHash],
            createdAt = row[Users.createdAt], // уже kotlinx.datetime.LocalDateTime
            role = row[Users.role]
        )
    }
}