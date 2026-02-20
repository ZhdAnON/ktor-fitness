package com.zhdanon.repository

import com.zhdanon.models.tables.Users
import com.zhdanon.models.domain.User
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class UserRepository {

    fun createUser(email: String, passwordHash: String, role: String = "USER"): User {
        val id = UUID.randomUUID()
        val now = Clock.System.now().toLocalDateTime(TimeZone.UTC)

        transaction {
            Users.insert {
                it[Users.id] = id
                it[Users.email] = email
                it[Users.passwordHash] = passwordHash
                it[Users.createdAt] = now
                it[Users.role] = role
                it[Users.refreshToken] = null
                it[Users.refreshExpiresAt] = null
            }
        }

        return User(id, email, passwordHash, now, role, null, null)
    }

    fun findByEmail(email: String): User? = transaction {
        Users.select { Users.email eq email }
            .map(::rowToUser)
            .singleOrNull()
    }

    fun findById(id: UUID): User? = transaction {
        Users.select { Users.id eq id }
            .map(::rowToUser)
            .singleOrNull()
    }

    fun findByRefreshToken(token: String): User? = transaction {
        Users.select { Users.refreshToken eq token }
            .map(::rowToUser)
            .singleOrNull()
    }

    fun updateRefreshToken(userId: UUID, token: String?, expiresAt: LocalDateTime?) {
        transaction {
            Users.update({ Users.id eq userId }) {
                it[refreshToken] = token
                it[refreshExpiresAt] = expiresAt
            }
        }
    }

    fun emailExists(email: String): Boolean = transaction {
        Users.select { Users.email eq email }.count() > 0
    }

    private fun rowToUser(row: ResultRow): User {
        return User(
            id = row[Users.id],
            email = row[Users.email],
            passwordHash = row[Users.passwordHash],
            createdAt = row[Users.createdAt],
            role = row[Users.role],
            refreshToken = row[Users.refreshToken],
            refreshExpiresAt = row[Users.refreshExpiresAt]
        )
    }
}