package com.zhdanon.models.mappers

import com.zhdanon.models.domain.Reps
import com.zhdanon.models.request.RepsRequest
import com.zhdanon.models.response.RepsResponse

fun RepsRequest.toDomain(): Reps = when (this) {
    is RepsRequest.Fixed -> Reps.Fixed(count)
    is RepsRequest.Range -> Reps.Range(from, to)
    is RepsRequest.TimeFixed -> Reps.TimeFixed(duration)
    is RepsRequest.TimeRange -> Reps.TimeRange(from, to)
    is RepsRequest.None -> Reps.None
}

fun Reps.toResponse(): RepsResponse =
    when (this) {
        is Reps.Fixed -> RepsResponse(
            type = "Fixed",
            count = count
        )
        is Reps.Range -> RepsResponse(
            type = "Range",
            from = from,
            to = to
        )
        is Reps.TimeRange -> RepsResponse(
            type = "TimeRange",
            from = from,
            to = to
        )
        is Reps.TimeFixed -> RepsResponse(
            type = "TimeFixed",
            duration = duration
        )
        Reps.None -> RepsResponse(
            type = "None"
        )
    }