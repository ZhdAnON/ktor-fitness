package com.zhdanon.models.mappers

import com.zhdanon.models.domain.Workout
import com.zhdanon.models.dto.WorkoutRequest
import com.zhdanon.models.response.WorkoutResponse
import kotlinx.datetime.LocalDate
import java.util.*

fun WorkoutRequest.toDomain(workoutId: UUID): Workout =
    Workout(
        id = workoutId,
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