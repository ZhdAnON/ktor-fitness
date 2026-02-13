package com.zhdanon.models.response

import kotlinx.serialization.Serializable

@Serializable
data class RoundsResponse(
    val type: String,
    val count: Int? = null,
    val from: Int? = null,
    val to: Int? = null
)