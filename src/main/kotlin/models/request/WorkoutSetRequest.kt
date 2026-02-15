package com.zhdanon.models.request

import com.zhdanon.models.domain.ProtocolType
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutSetRequest(
    val order: Int,
    val protocol: ProtocolType,
    val rounds: RoundsRequest,
    val exercises: List<SetExerciseRequest>,
    val note: String? = null
)