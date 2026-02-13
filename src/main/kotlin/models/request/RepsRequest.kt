package com.zhdanon.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class RepsRequest(
    val weight: Int,
    val count: Int
)