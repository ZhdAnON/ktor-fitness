package com.zhdanon.models.request

import kotlinx.serialization.Serializable

@Serializable
data class RepsRequest(
    val weight: Int,
    val count: Int
)