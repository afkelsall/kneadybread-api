package com.github.afkelsall.dynamodb

import com.kneadybread.builder.AttributeValueBuilder
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import java.math.BigDecimal


typealias PartitionKeySearch<P> = AttributeSearch.PartitionKeySearch<P>
typealias SortKeySearch<S> = AttributeSearch.SortKeySearch<S>
typealias SortKeyBeginsWith<S> = AttributeSearch.SortKeyBeginsWith<S>

//fun <P: Any> matchPartitionKey(partitionKeyValue: P): PartitionKeySearch<P> {
//    return PartitionKeySearch(partitionKeyValue, )
//}
//
//fun <S: Any> matchSortKey(partitionKeyValue: S): AttributeSearch<S> {
//    return SortKeySearch(partitionKeyValue)
//}
//
//fun <S: Any> sortKeyBeginsWith(partitionKeyValue: S): AttributeSearch<S> {
//    return SortKeyBeginsWith(partitionKeyValue)
//}

sealed class AttributeSearch<T: Any>(private val matchesValue: T, val keyInfo: KeyInfo) {

    fun getAttributeValue(): AttributeValue {
        return when (keyInfo) {
            is KeyInfo.SortKeyInfo.StringKey -> AttributeValueBuilder.string(matchesValue as String)
            is KeyInfo.SortKeyInfo.BigDecimalKey -> AttributeValueBuilder.bigDecimal(matchesValue as BigDecimal)
            is KeyInfo.SortKeyInfo.ByteArrayKey -> AttributeValueBuilder.binary(matchesValue as ByteArray)
            is KeyInfo.PartitionKeyInfo.StringKey -> AttributeValueBuilder.string(matchesValue as String)
            is KeyInfo.PartitionKeyInfo.BigDecimalKey -> AttributeValueBuilder.bigDecimal(matchesValue as BigDecimal)
            is KeyInfo.PartitionKeyInfo.ByteArrayKey -> AttributeValueBuilder.binary(matchesValue as ByteArray)
            is KeyInfo.SortKeyInfo.NoSortKey -> throw Exception("Should never have no sort key here.")
        }
    }

    open fun getSearchQuery(): String = "${keyInfo.keyName} = :${keyInfo.keyName}"

    class PartitionKeySearch<P: Any>(matchesString: P, keyInfo: KeyInfo.PartitionKeyInfo<P>): AttributeSearch<P>(matchesString, keyInfo)

    class SortKeySearch<S: Any>(matchesString: S, keyInfo: KeyInfo.SortKeyInfo<S>): AttributeSearch<S>(matchesString, keyInfo) {}

    class SortKeyBeginsWith<S: Any>(beginsWithString: S, keyInfo: KeyInfo.SortKeyInfo<S>): AttributeSearch<S>(beginsWithString, keyInfo) {
        override fun getSearchQuery(): String = "begins_with (${keyInfo.keyName}, :${keyInfo.keyName})"
    }
}



class AttributeSearchQueryBuilder<P: Any,S: Any> {
    private val searchKeyMap = mutableMapOf<String, AttributeSearch<*>>()

    fun addPrimaryKeySearch(searchString: P, partitionKeyAttributeName: KeyInfo.PartitionKeyInfo<P>): AttributeSearchQueryBuilder<P,S> {
        return addPrimaryKeySearch(AttributeSearch.PartitionKeySearch(searchString, partitionKeyAttributeName))
    }

    fun addPrimaryKeySearch(
        partitionKeySearch: PartitionKeySearch<P>
    ): AttributeSearchQueryBuilder<P,S> {
        searchKeyMap[partitionKeySearch.keyInfo.keyName] = partitionKeySearch
        return this
    }


    fun addSortKeySearch(searchString: S?, sortKeyInfo: KeyInfo.SortKeyInfo<S>?): AttributeSearchQueryBuilder<P,S> {
        if (searchString == null || sortKeyInfo == null) return this

        return addSortKeySearch(AttributeSearch.SortKeySearch(searchString, sortKeyInfo))
    }

    fun addSortKeySearch(sortKeySearch: AttributeSearch<S>?): AttributeSearchQueryBuilder<P,S> {
        if (sortKeySearch == null) return this

        searchKeyMap[sortKeySearch.keyInfo.keyName] = sortKeySearch
        return this
    }

    fun buildGetValues(): Map<String, AttributeValue> {
        return searchKeyMap.map {
            it.key to it.value.getAttributeValue()
        }.toMap()
    }

    fun buildSearchValues(): Map<String, AttributeValue> {
        return searchKeyMap.map {
            ":${it.key}" to it.value.getAttributeValue()
        }.toMap()
    }

    fun buildSearchExpression(): String {
        return searchKeyMap.values.joinToString(separator = " AND ") { it.getSearchQuery() }
    }


}