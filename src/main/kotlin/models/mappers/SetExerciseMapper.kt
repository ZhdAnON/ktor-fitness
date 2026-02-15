package com.zhdanon.models.mappers

import com.zhdanon.models.domain.SetExercise
import com.zhdanon.models.request.SetExerciseRequest
import com.zhdanon.models.response.SetExerciseResponse
import java.util.*

fun SetExerciseRequest.toDomain(): SetExercise =
    SetExercise(
        id = UUID.randomUUID(),
        exerciseId = UUID.fromString(exerciseId),
        reps = reps.toDomain(),
        note = note
    )

fun SetExercise.toResponse() = SetExerciseResponse(
    id = id.toString(),
    exerciseId = exerciseId.toString(),
    reps = reps.toResponse(),
    note = note
)