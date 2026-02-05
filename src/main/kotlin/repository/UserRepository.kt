package com.zhdanon.repository

import com.zhdanon.models.UserTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepository {

    fun createUser(email: String, passwordHash: String): Int = transaction {
        UserTable.insert {
            it[UserTable.email] = email
            it[UserTable.password] = passwordHash
        } get UserTable.id
    }

    fun findByEmail(email: String): ResultRow? = transaction {
        UserTable.select { UserTable.email eq email }.singleOrNull()
    }
}