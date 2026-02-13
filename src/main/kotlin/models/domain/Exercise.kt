package com.zhdanon.models.domain

import java.util.UUID

data class Exercise(
    val id: UUID,
    val name: String,
    val muscleGroups: List<String>,
    val technique: String,
    val videoUrl: String? = null
)