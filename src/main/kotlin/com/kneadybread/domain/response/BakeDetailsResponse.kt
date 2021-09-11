package com.kneadybread.domain.response

import com.kneadybread.domain.BakeDb
import com.kneadybread.domain.BakeDetails
import com.kneadybread.domain.BakeStep
import com.kneadybread.domain.DoughIngredients
import com.kneadybread.util.SortKeyPrefixes

data class BakeDetailsResponse(val bakerId: String,
                               val bakeId: String,
                               val details: BakeDetails,
                               val doughIngredients: DoughIngredients?,
                               val steps: List<BakeStep>?
) {
    companion object {
        fun from(bakeDb: BakeDb): BakeDetailsResponse {
            return BakeDetailsResponse(
                bakeDb.bakerId,
                bakeDb.sortKey.removePrefix(SortKeyPrefixes.BakeSortKey),
                bakeDb.details,
                bakeDb.doughIngredients,
                bakeDb.steps
            )
        }
    }
}