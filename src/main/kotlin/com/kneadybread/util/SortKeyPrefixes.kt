package com.kneadybread.util

class SortKeyPrefixes {
    companion object {
        const val BakeSortKey = "Bakes_"
    }
}

fun String.addPrefix(prefix: String): String {
    return prefix + this
}