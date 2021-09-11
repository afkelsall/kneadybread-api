package com.kneadybread.domain

import java.time.ZonedDateTime

data class KneadyDateTime (
    val date: ZonedDateTime,
    val originalZoneId: String
)