package com.zhdanon.models.response

import kotlinx.serialization.Serializable

@Serializable
data class RepsResponse(
    val type: String,
    val count: Int? = null,
    val from: Int? = null,
    val to: Int? = null,
    val duration: Int? = null
)