package com.zhdanon.models.tables

import org.jetbrains.exposed.sql.Table

object ExercisesTable : Table("exercises") {
    val id = uuid("id")
    val name = text("name")
    val muscleGroups = text("muscle_groups")
    val technique = text("technique")
    val videoUrl = text("video_url").nullable()

    override val primaryKey = PrimaryKey(id)
}