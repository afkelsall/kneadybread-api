package com.kneadybread.domain.request

import java.time.LocalDate

data class NewBakeRequest(
    val name: String,
    val description: String,
    val date: LocalDate,
)