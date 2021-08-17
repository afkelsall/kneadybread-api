package com.kneadybread.dao

import com.github.afkelsall.dynamodb.DynamoDbAsycClientWrapper
import com.github.afkelsall.dynamodb.sortKeyBeginsWith
import com.google.inject.Inject
import com.kneadybread.domain.Bake
import com.kneadybread.util.SortKeyPrefixes

class BakeDao @Inject constructor(private val client: DynamoDbAsycClientWrapper<String,String>) {

    fun createNewBake(bake: Bake) {
        client.putRecord(bake)
    }

    fun getBakeList(user: String): List<Bake> {
        return client.queryRecords(user, sortKeyBeginsWith(SortKeyPrefixes.BakeSortKey))
    }

}
