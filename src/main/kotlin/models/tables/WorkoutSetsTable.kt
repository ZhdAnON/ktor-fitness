package com.zhdanon.models.tables

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object WorkoutSetsTable : Table("workout_sets") {
    val id = uuid("id")
    val workoutId = reference("workout_id", WorkoutsTable.id, onDelete = ReferenceOption.CASCADE)
    val order = integer("set_order")
    val protocol = text("protocol")
    val rounds = text("rounds")
    val note = text("note").nullable()

    override val primaryKey = PrimaryKey(id)
}