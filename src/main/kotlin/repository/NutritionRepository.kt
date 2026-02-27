package com.zhdanon.repository

import com.zhdanon.database.DatabaseFactory.dbQuery
import com.zhdanon.models.domain.NutritionCategory
import com.zhdanon.models.domain.NutritionProgram
import com.zhdanon.models.request.CreateNutritionProgramRequest
import com.zhdanon.models.tables.NutritionProgramsTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import java.util.*

class NutritionRepository {

    suspend fun createProgram(request: CreateNutritionProgramRequest): NutritionProgram = dbQuery {
        val id = UUID.randomUUID()

        NutritionProgramsTable.insert {
            it[NutritionProgramsTable.id] = id
            it[category] = request.category.name
            it[text] = request.text
        }

        NutritionProgram(
            id = id,
            category = request.category,
            text = request.text
        )
    }

    suspend fun getAllPrograms(): List<NutritionProgram> = dbQuery {
        NutritionProgramsTable
            .selectAll()
            .map { row ->
                NutritionProgram(
                    id = row[NutritionProgramsTable.id],
                    category = NutritionCategory.valueOf(row[NutritionProgramsTable.category]),
                    text = row[NutritionProgramsTable.text]
                )
            }
    }

    suspend fun getProgramById(id: UUID): NutritionProgram? = dbQuery {
        NutritionProgramsTable
            .select { NutritionProgramsTable.id eq id }
            .map { row ->
                NutritionProgram(
                    id = row[NutritionProgramsTable.id],
                    category = NutritionCategory.valueOf(row[NutritionProgramsTable.category]),
                    text = row[NutritionProgramsTable.text]
                )
            }
            .singleOrNull()
    }

    suspend fun deleteProgram(id: UUID): Boolean = dbQuery {
        val deleted = NutritionProgramsTable.deleteWhere { NutritionProgramsTable.id eq id }
        deleted > 0
    }
}