package com.zhdanon.models.response

import kotlinx.serialization.Serializable

@Serializable
data class RepsResponse(
    val weight: Int,
    val count: Int
)