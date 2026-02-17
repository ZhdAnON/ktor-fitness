package com.zhdanon.models.mappers

import com.zhdanon.models.domain.Rounds
import com.zhdanon.models.request.RoundsRequest
import com.zhdanon.models.response.RoundsResponse

fun RoundsRequest.toDomain(): Rounds = when (this) {
    is RoundsRequest.Fixed -> Rounds.Fixed(count)
    is RoundsRequest.Range -> Rounds.Range(from, to)
    is RoundsRequest.TimeFixed -> Rounds.TimeFixed(duration)
    is RoundsRequest.TimeRange -> Rounds.TimeRange(from, to)
    is RoundsRequest.None -> Rounds.None
}

fun Rounds.toResponse(): RoundsResponse =
    when (this) {
        is Rounds.Fixed -> RoundsResponse(
            type = "Fixed",
            count = count
        )
        is Rounds.Range -> RoundsResponse(
            type = "Range",
            from = from,
            to = to
        )
        is Rounds.TimeRange -> RoundsResponse(
            type = "TimeRange",
            from = from,
            to = to
        )
        is Rounds.TimeFixed -> RoundsResponse(
            type = "TimeFixed",
            from = duration
        )
        Rounds.None -> RoundsResponse(
            type = "None"
        )
    }