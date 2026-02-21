package com.zhdanon.models.response

import kotlinx.serialization.Serializable

@Serializable
data class AddUserRequestDto(
    val email: String,
    val password: String,
    val role: String
)