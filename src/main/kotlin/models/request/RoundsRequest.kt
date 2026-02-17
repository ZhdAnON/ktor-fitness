package com.zhdanon.models.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class RoundsRequest {

    @Serializable @SerialName("Fixed")
    data class Fixed(val count: Int) : RoundsRequest()

    @Serializable @SerialName("Range")
    data class Range(val from: Int, val to: Int) : RoundsRequest()

    @Serializable @SerialName("TimeFixed")
    data class TimeFixed(val duration: Int) : RoundsRequest()

    @Serializable @SerialName("TimeRange")
    data class TimeRange(val from: Int, val to: Int) : RoundsRequest()

    @Serializable @SerialName("None")
    data object None : RoundsRequest()
}