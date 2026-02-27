package com.zhdanon.models.response

import com.zhdanon.models.domain.NutritionCategory
import kotlinx.serialization.Serializable

@Serializable
data class NutritionProgramResponse(
    val id: String,
    val category: NutritionCategory,
    val text: String
)