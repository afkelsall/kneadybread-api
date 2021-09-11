package com.kneadybread.domain

import com.kneadybread.domain.request.BakeDetailsRequest
import com.kneadybread.domain.request.BakeRequest
import com.kneadybread.util.SortKeyPrefixes
import com.kneadybread.util.addPrefix
import java.util.*

data class BakeDb(val bakerId: String,
                  val sortKey: String,
                  val details: BakeDetails,
                  val doughIngredients: DoughIngredients? = null,
                  val steps: List<BakeStep>? = null
) {
    companion object {
        fun newBakeFrom(user: String, bakeDetailsRequest: BakeDetailsRequest): BakeDb {
            return BakeDb(user,  UUID.randomUUID().toString().addPrefix(SortKeyPrefixes.BakeSortKey), BakeDetails.from(bakeDetailsRequest))
        }

        fun from(user: String, bakeDetailsRequest: BakeDetailsRequest): BakeDb {
            return BakeDb(user,  UUID.randomUUID().toString().addPrefix(SortKeyPrefixes.BakeSortKey), BakeDetails.from(bakeDetailsRequest))
        }

        fun fromUpdateRequest(user: String, bake: String, request: BakeRequest): BakeDb {
            return BakeDb(user, bake.addPrefix(SortKeyPrefixes.BakeSortKey), BakeDetails.from(request.details), request.doughIngredients, request.steps)
        }
    }
}


data class BakeDetails (
    val name: String,
    val description: String,
    val date: KneadyDateTime
) {

    companion object {
        fun from(bakeDetailsRequest: BakeDetailsRequest): BakeDetails {
            return BakeDetails(bakeDetailsRequest.name, bakeDetailsRequest.description, bakeDetailsRequest.date)
        }
    }
}
