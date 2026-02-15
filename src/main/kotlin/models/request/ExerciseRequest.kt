package com.zhdanon.models.request

import kotlinx.serialization.Serializable

@Serializable
data class ExerciseRequest(
    val name: String,
    val muscleGroups: List<String>,
    val technique: String,
    val videoUrl: String? = null
)