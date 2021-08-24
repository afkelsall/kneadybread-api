package com.kneadybread.domain

import com.kneadybread.domain.request.BakeRequest
import com.kneadybread.domain.request.KneadyDate
import com.kneadybread.util.SortKeyPrefixes
import com.kneadybread.util.addPrefix
import java.util.*

data class BakeDb(val bakerId: String,
                  val sortKey: String,
                  val details: BakeDetails
) {
    companion object {
        fun newBakeFrom(user: String, bakeRequest: BakeRequest): BakeDb {
            return BakeDb(user,  UUID.randomUUID().toString().addPrefix(SortKeyPrefixes.BakeSortKey), BakeDetails.from(bakeRequest))
        }

        fun from(user: String, bakeRequest: BakeRequest): BakeDb {
            return BakeDb(user,  UUID.randomUUID().toString().addPrefix(SortKeyPrefixes.BakeSortKey), BakeDetails.from(bakeRequest))
        }
    }
}



data class Bake(val bakerId: String,
                val bakeId: String,
                val details: BakeDetails
) {
    companion object {
        fun from(bakeDb: BakeDb): Bake {
            return Bake(bakeDb.bakerId, bakeDb.sortKey.removePrefix(SortKeyPrefixes.BakeSortKey), bakeDb.details)
        }
    }
}

data class BakeDetails (
    val name: String,
    val description: String,
    val date: KneadyDate
) {

    companion object {
        fun from(bakeRequest: BakeRequest): BakeDetails {
            return BakeDetails(bakeRequest.name, bakeRequest.description, bakeRequest.date)
        }
    }
}
