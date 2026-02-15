package com.zhdanon.models.mappers

import com.zhdanon.models.domain.Workout
import com.zhdanon.models.request.WorkoutRequest
import com.zhdanon.models.response.WorkoutDetailsResponse
import com.zhdanon.models.response.WorkoutResponse
import kotlinx.datetime.LocalDate
import java.util.*

fun WorkoutRequest.toDomain(workoutId: UUID, userId: UUID): Workout =
    Workout(
        id = workoutId,
        userId = userId,
        date = LocalDate.parse(date),
        title = title,
        sets = sets.map { it.toDomain() }
    )

fun Workout.toResponse() = WorkoutResponse(
    id = id.toString(),
    date = date.toString(),
    title = title,
    sets = sets.map { it.toResponse() }
)

fun Workout.toDetailsResponse() =
    WorkoutDetailsResponse(
        id = id.toString(),
        date = date.toString(),
        title = title,
        sets = sets.map { it.toResponse() }
    )