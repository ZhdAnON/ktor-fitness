package com.zhdanon.database.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object Workouts : Table("workouts") {
    val id = uuid("id").autoGenerate()
    val userId = uuid("user_id").references(Users.id)
    val date = datetime("date")
    val type = varchar("type", 100)
    val notes = varchar("notes", 500).nullable()

    override val primaryKey = PrimaryKey(id)
}