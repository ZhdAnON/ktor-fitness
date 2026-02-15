package com.zhdanon.models.domain

import kotlinx.serialization.Serializable

@Serializable
data class Reps(
    val weight: Int,
    val count: Int
)