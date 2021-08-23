package com.kneadybread.domain

import com.github.afkelsall.dynamodb.PartitionKey
import com.github.afkelsall.dynamodb.SortKey
import com.kneadybread.domain.request.KneadyDate
import com.kneadybread.domain.request.NewBakeRequest
import com.kneadybread.util.SortKeyPrefixes
import java.util.*

data class Bake(@PartitionKey("bakerId") val bakerId: String,
                @SortKey val sortKey: String,
                val details: BakeDetails,
) {

    companion object {
        fun from(user: String, newBakeRequest: NewBakeRequest): Bake {
            return (Bake(user,  SortKeyPrefixes.BakeSortKey + UUID.randomUUID().toString(), BakeDetails.from(newBakeRequest)))
        }
    }
}

data class BakeDetails (
    val name: String,
    val description: String,
    val date: KneadyDate
) {

    companion object {
        fun from(newBakeRequest: NewBakeRequest): BakeDetails {
            return BakeDetails(newBakeRequest.name, newBakeRequest.description, newBakeRequest.date)
        }
    }
}
