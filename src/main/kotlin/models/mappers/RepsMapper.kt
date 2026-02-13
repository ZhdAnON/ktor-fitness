package com.zhdanon.models.mappers

import com.zhdanon.models.domain.Reps
import com.zhdanon.models.dto.RepsRequest
import com.zhdanon.models.response.RepsResponse

fun RepsRequest.toDomain(): Reps =
    Reps(
        weight = weight,
        count = count
    )

fun Reps.toResponse() = RepsResponse(
    weight = weight,
    count = count
)