package com.zhdanon.models.request

import kotlinx.serialization.Serializable

@Serializable
data class SetExerciseRequest(
    val exerciseId: String,
    val reps: String,
    val note: String? = null
)