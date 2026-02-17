package com.zhdanon.models.request

import kotlinx.serialization.Serializable

@Serializable
data class SetExerciseRequest(
    val exerciseId: String,
    val reps: RepsRequest,
    val note: String? = null
)