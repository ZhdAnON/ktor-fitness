package com.zhdanon.models.domain

import kotlinx.datetime.LocalDate
import java.util.UUID

data class Workout(
    val id: UUID,
    val date: LocalDate,
    val title: String,
    val sets: List<WorkoutSet>
)