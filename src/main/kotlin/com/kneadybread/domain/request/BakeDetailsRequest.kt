package com.kneadybread.domain.request

import com.kneadybread.domain.BakeStep
import com.kneadybread.domain.DoughIngredients
import com.kneadybread.domain.KneadyDateTime

data class BakeRequest(
    val details: BakeDetailsRequest,
    val doughIngredients: DoughIngredients,
    val steps: List<BakeStep>
)

data class BakeDetailsRequest(
    val name: String,
    val description: String,
    val date: KneadyDateTime
)

