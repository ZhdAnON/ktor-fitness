package com.zhdanon.models.domain

import java.util.UUID

data class SetExercise(
    val id: UUID,
    val exerciseId: UUID,
    val reps: Reps,
    val note: String?
)