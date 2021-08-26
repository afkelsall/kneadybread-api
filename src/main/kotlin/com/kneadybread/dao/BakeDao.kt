package com.kneadybread.dao

import com.github.afkelsall.dynamodb.DynamoDbAsycClientWrapper
import com.github.afkelsall.dynamodb.sortKeyBeginsWith
import com.google.inject.Inject
import com.google.inject.name.Named
import com.kneadybread.domain.BakeDb
import com.kneadybread.util.SortKeyPrefixes

class BakeDao @Inject constructor(@Named("kneadybread") private val client: DynamoDbAsycClientWrapper<String,String>) {

    fun createNewBake(bakeDb: BakeDb) {
        client.putRecord(bakeDb)
    }

    fun getBakeList(user: String): List<BakeDb> {
        return client.queryRecords(user, sortKeyBeginsWith(SortKeyPrefixes.BakeSortKey))
    }

    fun getBake(user: String, bakeId: String): BakeDb {
        return client.getRecord(user, bakeId)
    }

    fun updateBake(user: String, bakeDb: BakeDb) {
        client.putRecord(bakeDb)
    }

}
