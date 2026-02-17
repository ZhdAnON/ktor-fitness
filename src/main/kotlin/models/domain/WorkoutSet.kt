package com.zhdanon.models.domain

import java.util.UUID

data class WorkoutSet(
    val id: UUID,
    val order: Int,
    val protocol: ProtocolType,
    val rounds: Rounds,
    val roundsJson: String,
    val exercises: List<SetExercise>,
    val note: String? = null
)