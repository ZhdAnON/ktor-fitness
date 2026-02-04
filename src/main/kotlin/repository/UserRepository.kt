package com.zhdanon.repository

import com.zhdanon.models.UserTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepository {

    fun createUser(email: String, password: String): Int = transaction {
        UserTable.insert {
            it[UserTable.email] = email
            it[UserTable.password] = password
        } get UserTable.id
    }

    fun findByEmail(email: String) = transaction {
        UserTable.select { UserTable.email eq email }.singleOrNull()
    }
}