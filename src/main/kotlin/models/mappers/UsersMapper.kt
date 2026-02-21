package com.zhdanon.models.mappers

import com.zhdanon.models.domain.User
import com.zhdanon.models.response.UserResponse

fun User.toResponse() = UserResponse(
    id = id.toString(),
    email = email,
    role = role,
    createdAt = createdAt.toString()
)
