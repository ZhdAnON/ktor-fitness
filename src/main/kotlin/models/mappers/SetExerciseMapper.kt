package com.zhdanon.models.mappers

import com.zhdanon.models.domain.SetExercise
import com.zhdanon.models.dto.SetExerciseRequest
import com.zhdanon.models.response.SetExerciseResponse
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import java.util.*

fun SetExerciseRequest.toDomain(): SetExercise {
    val repsJson = Json.encodeToString(reps)
    return SetExercise(
        id = UUID.fromString(id),
        exerciseId = UUID.fromString(exerciseId),
        reps = reps.toDomain(),      // доменная модель
        repsJson = repsJson,         // JSON-строка
        repsRequest = reps,          // DTO
        note = note
    )
}

fun SetExercise.toResponse() = SetExerciseResponse(
    id = id.toString(),
    exerciseId = exerciseId.toString(),
    reps = reps.toResponse(),
    note = note
)