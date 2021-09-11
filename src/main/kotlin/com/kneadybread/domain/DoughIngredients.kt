package com.kneadybread.domain

import java.math.BigDecimal

data class DoughIngredients (
    val starter: BigDecimal,
    val flour: BigDecimal,
    val water: BigDecimal,
    val salt: BigDecimal,
    val starterHydration: BigDecimal
)