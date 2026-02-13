package com.zhdanon.models.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class RoundsRequest {
    @Serializable
    @SerialName("Fixed")
    data class Fixed(val count: Int) : RoundsRequest()

    @Serializable
    @SerialName("Range")
    data class Range(val min: Int, val max: Int) : RoundsRequest()

    @Serializable
    @SerialName("Time")
    data class Time(val seconds: Int) : RoundsRequest()

    @Serializable
    @SerialName("None")
    data object None : RoundsRequest()
}