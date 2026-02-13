package com.zhdanon.models.domain

sealed class Rounds {
    data class Fixed(val count: Int) : Rounds()
    data class Range(val from: Int, val to: Int) : Rounds()
    data class Time(val from: Int, val to: Int) : Rounds()
    object None : Rounds()
}