package com.zhdanon.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutRequest(
    val id: String,
    val date: String,
    val title: String,
    val sets: List<WorkoutSetRequest>
)