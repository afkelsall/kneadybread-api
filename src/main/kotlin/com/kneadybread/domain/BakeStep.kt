package com.kneadybread.domain

data class BakeStep (
    val sequence: Int,
    val description: String,
    val notes: String,
    val plannedTime: KneadyDateTime,
    val actualTime: KneadyDateTime,
    val media: List<BakeMedia>
)