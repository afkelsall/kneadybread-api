package com.kneadybread.domain.request

import java.time.ZonedDateTime

data class BakeRequest(
    val name: String,
    val description: String,
    val date: KneadyDate
)

data class KneadyDate (
    val date: ZonedDateTime,
    val originalZoneId: String
)