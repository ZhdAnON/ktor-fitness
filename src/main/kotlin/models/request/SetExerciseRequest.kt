package com.zhdanon.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class SetExerciseRequest(
    val id: String,
    val exerciseId: String,
    val reps: RepsRequest,
    val note: String? = null
)