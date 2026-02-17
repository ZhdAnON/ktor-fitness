package com.zhdanon.models.response

import kotlinx.serialization.Serializable

@Serializable
data class SetExerciseResponse(
    val id: String,
    val exerciseId: String,
    val reps: String,
    val note: String?
)