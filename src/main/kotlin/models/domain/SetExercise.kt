package com.zhdanon.models.domain

import com.zhdanon.models.dto.RepsRequest
import java.util.UUID

data class SetExercise(
    val id: UUID,
    val exerciseId: UUID,
    val reps: Reps,
    val repsJson: String,
    val repsRequest: RepsRequest,
    val note: String? = null
)