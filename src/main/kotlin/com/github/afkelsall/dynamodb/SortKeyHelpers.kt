package com.github.afkelsall.dynamodb

fun <S> sortKeyBeginsWith(sortKey: S): SortKeyBeginsWithHelper<S> {
    return SortKeyBeginsWithHelper(sortKey)
}

data class SortKeyBeginsWithHelper<S>(val sortKey: S)