package com.zhdanon.models.dto

import com.zhdanon.models.domain.ProtocolType
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutSetRequest(
    val id: String,
    val order: Int,
    val protocol: ProtocolType,
    val rounds: RoundsRequest,
    val exercises: List<SetExerciseRequest>,
    val note: String? = null
)