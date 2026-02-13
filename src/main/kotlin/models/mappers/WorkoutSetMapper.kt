package com.zhdanon.models.mappers

import com.zhdanon.models.domain.WorkoutSet
import com.zhdanon.models.dto.WorkoutSetRequest
import com.zhdanon.models.response.WorkoutSetResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

fun WorkoutSetRequest.toDomain(): WorkoutSet =
    WorkoutSet(
        id = UUID.fromString(id),
        order = order,
        protocol = protocol,
        rounds = rounds.toDomain(),
        roundsJson = Json.encodeToString(rounds), // ← вот это добавляем
        exercises = exercises.map { it.toDomain() },
        note = note
    )

fun WorkoutSet.toResponse() = WorkoutSetResponse(
    id = id.toString(),
    order = order,
    protocol = protocol.name,
    rounds = rounds.toResponse(),
    exercises = exercises.map { it.toResponse() },
    note = note
)