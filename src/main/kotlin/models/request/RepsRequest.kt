package com.zhdanon.models.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class RepsRequest {

    @Serializable
    @SerialName("Fixed")
    data class Fixed(val count: Int) : RepsRequest()

    @Serializable
    @SerialName("Range")
    data class Range(val from: Int, val to: Int) : RepsRequest()

    @Serializable
    @SerialName("TimeFixed")
    data class TimeFixed(val duration: Int) : RepsRequest()

    @Serializable
    @SerialName("TimeRange")
    data class TimeRange(val from: Int, val to: Int) : RepsRequest()

    @Serializable
    @SerialName("None")
    data object None : RepsRequest()
}