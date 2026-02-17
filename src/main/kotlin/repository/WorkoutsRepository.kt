package com.zhdanon.repository

import com.zhdanon.database.DatabaseFactory.dbQuery
import com.zhdanon.models.tables.SetExercisesTable
import com.zhdanon.models.tables.WorkoutSetsTable
import com.zhdanon.models.tables.WorkoutsTable
import com.zhdanon.models.request.RoundsRequest
import com.zhdanon.models.mappers.toDomain
import com.zhdanon.models.domain.ProtocolType
import com.zhdanon.models.domain.SetExercise
import com.zhdanon.models.domain.Workout
import com.zhdanon.models.domain.WorkoutSet
import com.zhdanon.models.request.RepsRequest
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.*
import java.util.*
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inSubQuery
import kotlinx.serialization.encodeToString


class WorkoutsRepository {

    suspend fun createWorkout(userId: UUID, workout: Workout): Workout = dbQuery {
        WorkoutsTable.insert {
            it[id] = workout.id
            it[WorkoutsTable.userId] = userId
            it[date] = workout.date
            it[title] = workout.title
        }

        workout.sets.forEach { set ->
            WorkoutSetsTable.insert {
                it[id] = set.id
                it[workoutId] = workout.id
                it[order] = set.order
                it[protocol] = set.protocol.name
                it[rounds] = Json.encodeToString(set.roundsRequest)
                it[note] = set.note
            }

            set.exercises.forEach { ex ->
                SetExercisesTable.insert {
                    it[id] = ex.id
                    it[setId] = set.id
                    it[exerciseId] = ex.exerciseId
                    it[reps] = Json.encodeToString(ex.repsRequest)
                    it[note] = ex.note
                }
            }
        }

        workout
    }

    suspend fun getWorkouts(): List<Workout> = dbQuery {
        WorkoutsTable
            .selectAll()
            .map { row ->
                val workoutId = row[WorkoutsTable.id]

                val sets = WorkoutSetsTable
                    .select { WorkoutSetsTable.workoutId eq workoutId }
                    .map { setRow ->
                        val setId = setRow[WorkoutSetsTable.id]

                        val exercises = SetExercisesTable
                            .select { SetExercisesTable.setId eq setId }
                            .map { exRow ->
                                val repsJson = exRow[SetExercisesTable.reps]
                                val repsRequest = Json.decodeFromString<RepsRequest>(repsJson)
                                val repsDomain = repsRequest.toDomain()
                                SetExercise(
                                    id = exRow[SetExercisesTable.id],
                                    exerciseId = exRow[SetExercisesTable.exerciseId],
                                    reps = repsDomain,
                                    repsRequest = repsRequest,
                                    note = exRow[SetExercisesTable.note]
                                )
                            }

                        val roundsRequest = Json.decodeFromString<RoundsRequest>(setRow[WorkoutSetsTable.rounds])
                        WorkoutSet(
                            id = setId,
                            order = setRow[WorkoutSetsTable.order],
                            protocol = ProtocolType.valueOf(setRow[WorkoutSetsTable.protocol]),
                            rounds = roundsRequest.toDomain(),
                            roundsRequest = roundsRequest,
                            exercises = exercises,
                            note = setRow[WorkoutSetsTable.note]
                        )
                    }

                Workout(
                    id = workoutId,
                    userId = row[WorkoutsTable.userId],
                    date = row[WorkoutsTable.date],
                    title = row[WorkoutsTable.title],
                    sets = sets
                )
            }
    }

    suspend fun getWorkoutById(workoutId: UUID): Workout? = dbQuery {
        WorkoutsTable
            .select { WorkoutsTable.id eq workoutId }
            .map { row ->
                val sets = WorkoutSetsTable
                    .select { WorkoutSetsTable.workoutId eq workoutId }
                    .map { setRow ->
                        val setId = setRow[WorkoutSetsTable.id]

                        val exercises = SetExercisesTable
                            .select { SetExercisesTable.setId eq setId }
                            .map { exRow ->
                                val repsJson = exRow[SetExercisesTable.reps]
                                val repsRequest = Json.decodeFromString<RepsRequest>(repsJson)
                                val repsDomain = repsRequest.toDomain()
                                SetExercise(
                                    id = exRow[SetExercisesTable.id],
                                    exerciseId = exRow[SetExercisesTable.exerciseId],
                                    reps = repsDomain,
                                    repsRequest = repsRequest,
                                    note = exRow[SetExercisesTable.note]
                                )
                            }

                        val roundsRequest = Json.decodeFromString<RoundsRequest>(setRow[WorkoutSetsTable.rounds])

                        WorkoutSet(
                            id = setId,
                            order = setRow[WorkoutSetsTable.order],
                            protocol = ProtocolType.valueOf(setRow[WorkoutSetsTable.protocol]),
                            rounds = roundsRequest.toDomain(),
                            roundsRequest = roundsRequest,
                            exercises = exercises,
                            note = setRow[WorkoutSetsTable.note]
                        )
                    }

                Workout(
                    id = workoutId,
                    userId = row[WorkoutsTable.userId],
                    date = row[WorkoutsTable.date],
                    title = row[WorkoutsTable.title],
                    sets = sets
                )
            }
            .singleOrNull()
    }

    suspend fun deleteWorkout(userId: UUID, workoutId: UUID): Boolean = dbQuery {
        // Проверяем, что тренировка принадлежит пользователю
        val exists = WorkoutsTable
            .select { (WorkoutsTable.id eq workoutId) and (WorkoutsTable.userId eq userId) }
            .count() > 0

        if (!exists) return@dbQuery false

        // Удаляем упражнения → сеты → тренировку
        SetExercisesTable.deleteWhere { setId inSubQuery
                WorkoutSetsTable.slice(WorkoutSetsTable.id)
                    .select { WorkoutSetsTable.workoutId eq workoutId }
        }

        WorkoutSetsTable.deleteWhere { WorkoutSetsTable.workoutId eq workoutId }

        WorkoutsTable.deleteWhere { id eq workoutId }

        true
    }

    suspend fun updateWorkout(userId: UUID, workoutId: UUID, workout: Workout): Boolean = dbQuery {

        val exists = WorkoutsTable
            .select { (WorkoutsTable.id eq workoutId) and (WorkoutsTable.userId eq userId) }
            .count() > 0

        if (!exists) return@dbQuery false

        // Удаляем старые данные
        SetExercisesTable.deleteWhere {
            setId inSubQuery
                    WorkoutSetsTable.slice(WorkoutSetsTable.id)
                        .select { WorkoutSetsTable.workoutId eq workoutId }
        }

        WorkoutSetsTable.deleteWhere { WorkoutSetsTable.workoutId eq workoutId }

        // Обновляем заголовок и дату
        WorkoutsTable.update({ WorkoutsTable.id eq workoutId }) {
            it[date] = workout.date
            it[title] = workout.title
        }

        // Добавляем новые сеты
        workout.sets.forEach { set ->
            WorkoutSetsTable.insert { row ->
                row[id] = set.id
                row[WorkoutSetsTable.workoutId] = workoutId
                row[order] = set.order
                row[protocol] = set.protocol.name
                row[rounds] = Json.encodeToString(set.roundsRequest)
                row[note] = set.note
            }

            set.exercises.forEach { ex ->
                SetExercisesTable.insert { row ->
                    row[id] = ex.id
                    row[setId] = set.id
                    row[exerciseId] = ex.exerciseId
                    row[reps] = Json.encodeToString(ex.repsRequest)
                    row[note] = ex.note
                }
            }
        }
        true
    }
}