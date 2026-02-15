package com.zhdanon.models.mappers

import com.zhdanon.models.domain.WorkoutSet
import com.zhdanon.models.request.WorkoutSetRequest
import com.zhdanon.models.response.WorkoutSetResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

fun WorkoutSetRequest.toDomain(): WorkoutSet =
    WorkoutSet(
        id = UUID.randomUUID(),
        order = order,
        protocol = protocol,
        rounds = rounds.toDomain(),
        roundsJson = Json.encodeToString(rounds), // ← вот это добавляем
        exercises = exercises.map { it.toDomain() },
        note = note
    )

fun WorkoutSet.toResponse() = WorkoutSetResponse(
    id = UUID.randomUUID().toString(),
    order = order,
    protocol = protocol.name,
    rounds = rounds.toResponse(),
    exercises = exercises.map { it.toResponse() },
    note = note
)