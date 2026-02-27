package com.zhdanon.models.request

import com.zhdanon.models.domain.NutritionCategory
import kotlinx.serialization.Serializable

@Serializable
data class CreateNutritionProgramRequest(
    val category: NutritionCategory,
    val text: String
)