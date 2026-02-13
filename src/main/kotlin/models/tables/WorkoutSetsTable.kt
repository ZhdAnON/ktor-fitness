package com.zhdanon.models.tables

import org.jetbrains.exposed.sql.Table

object WorkoutSetsTable : Table("workout_sets") {
    val id = uuid("id")
    val workoutId = uuid("workout_id").references(WorkoutsTable.id)
    val order = integer("set_order")
    val protocol = text("protocol")
    val rounds = text("rounds")
    val note = text("note").nullable()

    override val primaryKey = PrimaryKey(id)
}