package com.github.afkelsall.dynamodb

import com.kneadybread.builder.AttributeValueBuilder
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

class AttributeValueMapper {

    inline fun <reified T: Any> getPartitionKeyAttributeName(): String {
        T::class.declaredMemberProperties.forEach { property ->
            val partitionKeyAttributeName =  property
                .findAnnotation<PartitionKey>()
                ?.let { it.attributeName.ifEmpty { property.name } }

            if (partitionKeyAttributeName != null)
                return partitionKeyAttributeName
        }

        throw Exception("PartitionKey annotation must be present on a field in the class.")
    }

    inline fun <reified T: Any> getSortKeyAttributeName(): String {
        T::class.declaredMemberProperties.forEach { property ->
            val partitionKeyAttributeName =  property
                .findAnnotation<SortKey>()
                ?.let { it.attributeName.ifEmpty { property.name } }

            if (partitionKeyAttributeName != null)
                return partitionKeyAttributeName
        }

        throw Exception("Sort annotation must be present on a field in the class.")
    }

    fun from(entity: Any): Map<String, AttributeValue> {
        val map = mutableMapOf<String, AttributeValue>()

        entity::class.memberProperties.forEach { property ->
            val partitionKeyName = property.findAnnotation<PartitionKey>()?.let { it.attributeName.ifEmpty { property.name } }
            val sortKeyName = property.findAnnotation<SortKey>()?.let { it.attributeName.ifEmpty { property.name } }

            if (partitionKeyName != null)
                map[partitionKeyName] = AttributeValue.builder().s(property.getter.call(entity) as String).build()
            else if (sortKeyName != null)
                map[sortKeyName] = AttributeValue.builder().s(property.getter.call(entity) as String).build()
        }

        map.putAll(addMemberValues(entity))

        return map
    }

    private fun addMemberValues(valueContainer: Any): Map<String, AttributeValue> {
        return valueContainer::class.declaredMemberProperties.associate { property ->
            property.name to getAttributeValue(property.getter.call(valueContainer))
        }
    }

    private fun addListValues(list: List<Any?>): List<AttributeValue> {
        return list.filterNotNull().map {
            getAttributeValue(it)
        }
    }

    private fun getAttributeValue(propertyValue: Any?): AttributeValue {
        if (propertyValue == null)
            return AttributeValueBuilder.nul()

        return when (propertyValue) {
            is String -> AttributeValueBuilder.string(propertyValue)
            is Int -> AttributeValueBuilder.int(propertyValue)
            is BigDecimal -> AttributeValueBuilder.bigDecimal(propertyValue)
            is ByteArray -> AttributeValueBuilder.binary(propertyValue)
            is Boolean -> AttributeValueBuilder.boolean(propertyValue)
            is LocalDate -> AttributeValueBuilder.string(propertyValue.format(DateTimeFormatter.ISO_DATE))
            is List<*> -> AttributeValueBuilder.list(addListValues(propertyValue))
            is Map<*, *> -> AttributeValueBuilder.map(addMapValues(propertyValue))

            else -> {
                if (propertyValue::class.declaredMemberProperties.isNotEmpty()) {
                    AttributeValueBuilder.map(addMemberValues(propertyValue))
                } else
                    throw RuntimeException("Unmapped property type")
            }
        }
    }

    private fun addMapValues(map: Map<*,*>): Map<String, AttributeValue> {
        return map.map {
            it.key as String to getAttributeValue(it.value)
        }.toMap()
    }
}

fun <T: Any> getPartitionKeyAttributeName(clazz: KClass<T>): String {
    clazz.declaredMemberProperties.forEach { property ->
        val partitionKeyAttributeName =  property
            .findAnnotation<PartitionKey>()
            ?.let { it.attributeName.ifEmpty { property.name } }

        if (partitionKeyAttributeName != null)
            return partitionKeyAttributeName
    }

    throw Exception("PartitionKey annotation must be present on a field in the class.")
}

fun <T: Any> getSortKeyAttributeName(clazz: KClass<T>): String {
    clazz.declaredMemberProperties.forEach { property ->
        val partitionKeyAttributeName =  property
            .findAnnotation<SortKey>()
            ?.let { it.attributeName.ifEmpty { property.name } }

        if (partitionKeyAttributeName != null)
            return partitionKeyAttributeName
    }

    throw Exception("Sort annotation must be present on a field in the class.")
}