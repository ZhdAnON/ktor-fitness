package com.zhdanon.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class ExerciseRequest(
    val id: String,
    val name: String,
    val muscleGroups: List<String>,
    val technique: String,
    val videoUrl: String? = null
)