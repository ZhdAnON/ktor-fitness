package com.zhdanon.models.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.date
import java.util.UUID

object WorkoutsTable : Table("workouts") {
    val id: Column<UUID> = uuid("id")
    val userId: Column<UUID> = uuid("user_id").references(Users.id)
    val date = date("date")
    val title = text("title")

    override val primaryKey = PrimaryKey(id)
}