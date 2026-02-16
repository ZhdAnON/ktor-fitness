package com.zhdanon.repository

import com.zhdanon.database.DatabaseFactory.dbQuery
import com.zhdanon.models.domain.Exercise
import com.zhdanon.models.request.ExerciseRequest
import com.zhdanon.models.tables.ExercisesTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

class ExerciseRepository {

    suspend fun getAll(): List<Exercise> = dbQuery {
        ExercisesTable.selectAll().map { row ->
            Exercise(
                id = row[ExercisesTable.id],
                name = row[ExercisesTable.name],
                muscleGroups = row[ExercisesTable.muscleGroups].split(","),
                technique = row[ExercisesTable.technique],
                videoUrl = row[ExercisesTable.videoUrl]
            )
        }
    }

    suspend fun getById(id: UUID): Exercise? = dbQuery {
        ExercisesTable.select { ExercisesTable.id eq id }
            .map { row ->
                Exercise(
                    id = row[ExercisesTable.id],
                    name = row[ExercisesTable.name],
                    muscleGroups = row[ExercisesTable.muscleGroups].split(","),
                    technique = row[ExercisesTable.technique],
                    videoUrl = row[ExercisesTable.videoUrl]
                )
            }
            .singleOrNull()
    }

    suspend fun create(req: ExerciseRequest): Exercise = dbQuery {
        val id = UUID.randomUUID()
        ExercisesTable.insert {
            it[ExercisesTable.id] = id
            it[name] = req.name
            it[muscleGroups] = req.muscleGroups.joinToString(",")
            it[technique] = req.technique
            it[videoUrl] = req.videoUrl
        }
        Exercise(id, req.name, req.muscleGroups, req.technique, req.videoUrl)
    }

    suspend fun update(id: UUID, req: ExerciseRequest): Boolean = dbQuery {
        ExercisesTable.update({ ExercisesTable.id eq id }) {
            it[name] = req.name
            it[muscleGroups] = req.muscleGroups.joinToString(",")
            it[technique] = req.technique
            it[videoUrl] = req.videoUrl
        } > 0
    }

    suspend fun delete(id: UUID): Boolean = dbQuery {
        ExercisesTable.deleteWhere { ExercisesTable.id eq id } > 0
    }
}