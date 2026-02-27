package com.zhdanon.models.tables

import org.jetbrains.exposed.sql.Table

object NutritionProgramsTable : Table("nutrition_programs") {
    val id = uuid("id")
    val category = varchar("category", 100)
    val text = text("text")

    override val primaryKey = PrimaryKey(id)
}