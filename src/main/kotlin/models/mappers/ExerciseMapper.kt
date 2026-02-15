package com.zhdanon.models.mappers

import com.zhdanon.models.domain.Exercise
import com.zhdanon.models.request.ExerciseRequest
import com.zhdanon.models.response.ExerciseResponse
import java.util.*

fun ExerciseRequest.toDomain(): Exercise =
    Exercise(
        id = UUID.randomUUID(),
        name = name,
        muscleGroups = muscleGroups,
        technique = technique,
        videoUrl = videoUrl
    )

fun Exercise.toResponse() = ExerciseResponse(
    id = id.toString(),
    name = name,
    muscleGroups = muscleGroups,
    technique = technique,
    videoUrl = videoUrl
)