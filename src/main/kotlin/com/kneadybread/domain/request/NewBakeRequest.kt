package com.kneadybread.domain.request

import java.time.ZonedDateTime

data class NewBakeRequest(
    val name: String,
    val description: String,
    val date: KneadyDate,
)

data class KneadyDate (
    val date: ZonedDateTime,
    val originalZoneId: String
)