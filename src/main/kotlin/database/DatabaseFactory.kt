package com.zhdanon.database

import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import com.zhdanon.models.tables.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object DatabaseFactory {


    fun init() {
        val config = ConfigFactory.load().getConfig("ktor.database")

        val hikariConfig = HikariConfig().apply {
            driverClassName = config.getString("driver")
            jdbcUrl = config.getString("url")
            username = config.getString("user")
            password = config.getString("password")
            maximumPoolSize = 10
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }

        val dataSource = HikariDataSource(hikariConfig)
        Database.connect(dataSource)

        transaction {
            SchemaUtils.create(
                Users,
                WorkoutsTable,
                WorkoutSetsTable,
                SetExercisesTable,
                ExercisesTable
            )
        }
        try {
            transaction {
                exec("SELECT 1;")
            }
        } catch (e: Exception) {
            println("DATABASE ERROR: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}