package com.zhdanon.models.response

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutSetResponse(
    val id: String,
    val order: Int,
    val protocol: String,
    val rounds: RoundsResponse,
    val exercises: List<SetExerciseResponse>,
    val note: String?
)