package com.zhdanon.models.request

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutRequest(
    val date: String,
    val title: String,
    val sets: List<WorkoutSetRequest>
)