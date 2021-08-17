package com.github.afkelsall.dynamodb

import java.math.BigDecimal
import kotlin.reflect.KClass

sealed class KeyInfo(val keyName: String, val keyType: KClass<*>) {
    sealed class PartitionKeyInfo<T : Any>(keyName: String, keyType: KClass<T>): KeyInfo(keyName, keyType) {
        class StringKey(keyName: String) : PartitionKeyInfo<String>(keyName, String::class)
        class BigDecimalKey(keyName: String) : PartitionKeyInfo<BigDecimal>(keyName, BigDecimal::class)
        class ByteArrayKey(keyName: String) : PartitionKeyInfo<ByteArray>(keyName, ByteArray::class)
    }

    sealed class SortKeyInfo<T : Any>(keyName: String, keyType: KClass<T>): KeyInfo(keyName, keyType) {
        class NoSortKey: SortKeyInfo<Nothing>("", Nothing::class)
        class StringKey(keyName: String) : SortKeyInfo<String>(keyName, String::class)
        class BigDecimalKey(keyName: String) : SortKeyInfo<BigDecimal>(keyName, BigDecimal::class)
        class ByteArrayKey(keyName: String) : SortKeyInfo<ByteArray>(keyName, ByteArray::class)
    }
}