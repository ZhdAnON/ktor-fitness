package com.zhdanon.models.response

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutResponse(
    val id: String,
    val date: String,
    val title: String,
    val sets: List<WorkoutSetResponse>
)