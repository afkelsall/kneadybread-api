package com.kneadybread.domain

data class BakeMedia (
    val type: BakeMediaType,
    val mediaId: String
)

enum class BakeMediaType {
    Image,
    Video
}