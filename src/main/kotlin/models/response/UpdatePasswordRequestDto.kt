package com.zhdanon.models.response

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePasswordRequestDto(
    val password: String
)