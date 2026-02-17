package com.zhdanon.models.domain

import com.zhdanon.models.request.RoundsRequest
import java.util.UUID

data class WorkoutSet(
    val id: UUID,
    val order: Int,
    val protocol: ProtocolType,
    val rounds: Rounds,
    val roundsRequest: RoundsRequest,
    val exercises: List<SetExercise>,
    val note: String? = null
)