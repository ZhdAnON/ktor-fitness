package com.zhdanon.models.response

import kotlinx.serialization.Serializable

@Serializable
data class ExerciseResponse(
    val id: String,
    val name: String,
    val muscleGroups: List<String>,
    val technique: String,
    val videoUrl: String? = null
)