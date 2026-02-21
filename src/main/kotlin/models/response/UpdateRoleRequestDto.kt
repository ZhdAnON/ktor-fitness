package com.zhdanon.models.response

import kotlinx.serialization.Serializable

@Serializable
data class UpdateRoleRequestDto(
    val role: String
)