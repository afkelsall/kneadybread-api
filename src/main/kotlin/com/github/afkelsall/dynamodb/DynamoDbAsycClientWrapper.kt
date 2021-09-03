package com.github.afkelsall.dynamodb

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.github.afkelsall.dynamodb.KeyInfo.PartitionKeyInfo
import com.github.afkelsall.dynamodb.KeyInfo.SortKeyInfo
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.http.async.SdkAsyncHttpClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.model.*
import java.net.URI
import kotlin.reflect.KClass


class DynamoDbAsycClientWrapper<P: Any,S: Any> (
    private val asyncHttpClient: SdkAsyncHttpClient,
    private val region: Region,
    private val awsCredentialsProvider: AwsCredentialsProvider,
    private val tableName: String,
    private val partitionKeyAttributeName: PartitionKeyInfo<P>,
    private val sortKeyAttributeName: SortKeyInfo<S>,
    private val mapper: ObjectMapper = DynamoDbObjectMapper().getMapper(),
    private val uriOverride: URI? = null
) {

    private val client: DynamoDbAsyncClient

    init {
        val clientBuilder = DynamoDbAsyncClient.builder()
            .httpClient(asyncHttpClient)
            .credentialsProvider(awsCredentialsProvider)

        if (uriOverride != null)
            clientBuilder.endpointOverride(uriOverride)
        else
            clientBuilder.region(region)

        client = clientBuilder.build()
    }


    fun <T: Any> putRecord(record: T): PutItemResponse {
        val itemToSave = mapper.convertValue<Map<String, AttributeValue>>(record)

        val putItemRequest = PutItemRequest.builder()
            .tableName(tableName)
            .item(itemToSave)
            .build()

        return client.putItem(putItemRequest).join()
    }

    fun <T: Any> updateRecord(partitionKey: P, sortKey: S? = null, record: T): UpdateItemResponse {
        throw Exception("Not build")

//        val itemToSave = mapper.convertValue<Map<String, AttributeValue>>(record)
//
//        val searchKeys = AttributeSearchQueryBuilder<P,S>()
//            .addPrimaryKeySearch(AttributeSearch.PartitionKeySearch(partitionKey, partitionKeyAttributeName))
//            .addSortKeySearch(sortKey?.let { AttributeSearch.SortKeySearch(it, sortKeyAttributeName) })
//
//        val updateItemRequest = UpdateItemRequest.builder()
//            .tableName(tableName)
//            .key(searchKeys.buildGetValues())
//            .updateExpression()
//            .build()
//
//        return client.updateItem(updateItemRequest).join()
    }


    // betterClient.getRecord<Bake>("axk", "5d0bc82d-8d94-43bf-93ae-f01968efab9c")
    inline fun <reified T : Any> getRecord(partitionKey: P, sortKey: S? = null): T {
        return getRecord(partitionKey, sortKey, T::class)
    }

    fun <T : Any> getRecord(partitionKey: P, sortKey: S? = null, resultClassType: KClass<T>): T {
        val searchKeys = AttributeSearchQueryBuilder<P,S>()
            .addPrimaryKeySearch(partitionKey, partitionKeyAttributeName)
            .addSortKeySearch(sortKey, sortKeyAttributeName)
            .buildGetValues()

        val getItemRequest = GetItemRequest.builder()
            .tableName(tableName)
            .key(searchKeys)
            .build()

        val item = client.getItem(getItemRequest)

        return mapper.convertValue(item.join().item(), resultClassType.java)
    }



    inline fun <reified T : Any> queryRecords(partitionKey: P, sortKey: S? = null): List<T> {
        return queryRecords(partitionKey, sortKey, T::class)
    }

    fun <T : Any> queryRecords(partitionKey: P, sortKey: S? = null, classOut: KClass<T>): List<T> {
        return queryRecords(
            AttributeSearch.PartitionKeySearch(partitionKey, partitionKeyAttributeName),
            sortKey?.let { AttributeSearch.SortKeySearch(it, sortKeyAttributeName) },
            classOut
        )
    }


    // betterClient.queryRecords<Bake>("axk", sortKeyBeginsWith("Bakes_"))
    inline fun <reified T : Any> queryRecords(partitionKey: P, sortKeyBeginsWithHelper: SortKeyBeginsWithHelper<S>): List<T> {
        return queryRecords(partitionKey, sortKeyBeginsWithHelper, T::class)
    }

    fun <T: Any> queryRecords(
        partitionKey: P,
        sortKeyBeginsWithHelper: SortKeyBeginsWithHelper<S>,
        classOut: KClass<T>
    ): List<T> {
        return queryRecords(
            AttributeSearch.PartitionKeySearch(partitionKey, partitionKeyAttributeName),
            AttributeSearch.SortKeyBeginsWith(sortKeyBeginsWithHelper.sortKey, sortKeyAttributeName),
            classOut
        )
    }

    private fun <T : Any> queryRecords(
        partitionKey: AttributeSearch.PartitionKeySearch<P>,
        sortKey: AttributeSearch<S>?,
        resultClassType: KClass<T>
    ): List<T> {
        val searchKeys = AttributeSearchQueryBuilder<P,S>()
            .addPrimaryKeySearch(partitionKey)
            .addSortKeySearch(sortKey)

        val queryRequest = QueryRequest.builder()
            .tableName(tableName)
            .keyConditionExpression(searchKeys.buildSearchExpression())
            .expressionAttributeValues(searchKeys.buildSearchValues())
            .build()

        val queryResult = client.query(queryRequest)

        val outputCollectionType = mapper.typeFactory.constructCollectionType(ArrayList::class.java, resultClassType.java)

        return mapper.convertValue(queryResult.join().items(), outputCollectionType)
    }

    fun deleteRecord(partitionKey: P, sortKey: S? = null) {

        val searchKeys = AttributeSearchQueryBuilder<P,S>()
            .addPrimaryKeySearch(AttributeSearch.PartitionKeySearch(partitionKey, partitionKeyAttributeName))
            .addSortKeySearch(sortKey?.let { AttributeSearch.SortKeySearch(sortKey, sortKeyAttributeName) })

        val deleteItemRequest = DeleteItemRequest.builder()
            .tableName(tableName)
            .key(searchKeys.buildGetValues())
            .build()

        val queryResult = client.deleteItem(deleteItemRequest)

        queryResult.join()
        return
    }


}