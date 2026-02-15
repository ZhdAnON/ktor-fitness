package com.zhdanon.models.tables

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object SetExercisesTable : Table("set_exercises") {
    val id = uuid("id")
    val setId = reference("set_id", WorkoutSetsTable.id, onDelete = ReferenceOption.CASCADE)
    val exerciseId = uuid("exercise_id").references(ExercisesTable.id)
    val reps = text("reps")
    val note = text("note").nullable()

    override val primaryKey = PrimaryKey(id)
}