package com.zhdanon.models.domain

import java.util.*

data class NutritionProgram(
    val id: UUID,
    val category: NutritionCategory,
    val text: String
)