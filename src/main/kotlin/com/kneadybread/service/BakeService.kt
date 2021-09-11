package com.kneadybread.service

import com.google.inject.Inject
import com.kneadybread.dao.BakeDao
import com.kneadybread.dao.UserDao
import com.kneadybread.domain.BakeDb
import com.kneadybread.domain.request.BakeDetailsRequest
import com.kneadybread.domain.request.BakeRequest
import com.kneadybread.domain.response.BakeDetailsResponse
import com.kneadybread.util.SortKeyPrefixes
import com.kneadybread.util.addPrefix

class BakeService @Inject constructor(private val userDao: UserDao, private val bakeDao: BakeDao) {



    fun saveNewBake(user: String, bakeDetailsRequest: BakeDetailsRequest) {
        val bakeDb = BakeDb.newBakeFrom(user, bakeDetailsRequest)

        bakeDao.createNewBake(bakeDb)
    }

    fun getBakeList(user: String): List<BakeDetailsResponse> {
        val out = bakeDao.getBakeList(user)
        return out.map { BakeDetailsResponse.from(it) }
    }

    fun saveBake(user: String, bakeDetailsRequest: BakeDetailsRequest): BakeDetailsRequest {
        val bakeDb = BakeDb.newBakeFrom(user, bakeDetailsRequest)

        bakeDao.updateBake(bakeDb)
        return bakeDetailsRequest
    }

    fun getBake(user: String, bakeId: String): BakeDetailsResponse {
        val bakeDb = bakeDao.getBake(user, bakeId.addPrefix(SortKeyPrefixes.BakeSortKey))
        return BakeDetailsResponse.from(bakeDb)
    }

    fun deleteBake(user: String, bakeId: String) {
        bakeDao.deleteBake(user, bakeId.addPrefix(SortKeyPrefixes.BakeSortKey))
    }

    fun updateBake(user: String, bake: String, request: BakeRequest) {
        val bakeDb = BakeDb.fromUpdateRequest(user, bake, request)
        bakeDao.updateBake(bakeDb)
    }

}
