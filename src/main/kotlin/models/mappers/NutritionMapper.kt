package com.zhdanon.models.mappers

import com.zhdanon.models.domain.NutritionProgram
import com.zhdanon.models.request.CreateNutritionProgramRequest
import com.zhdanon.models.response.NutritionProgramResponse
import java.util.*

fun NutritionProgram.toResponse() = NutritionProgramResponse(
    id = id.toString(),
    category = category,
    text = text
)

fun CreateNutritionProgramRequest.toDomain(programId: UUID): NutritionProgram =
    NutritionProgram(
        id = programId,
        category = category,
        text = text
    )