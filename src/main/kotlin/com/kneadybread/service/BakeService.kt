package com.kneadybread.service

import com.google.inject.Inject
import com.kneadybread.dao.BakeDao
import com.kneadybread.dao.UserDao
import com.kneadybread.domain.Bake
import com.kneadybread.domain.BakeDb
import com.kneadybread.domain.request.BakeRequest
import com.kneadybread.util.SortKeyPrefixes
import com.kneadybread.util.addPrefix

class BakeService @Inject constructor(private val userDao: UserDao, private val bakeDao: BakeDao) {



    fun saveNewBake(user: String, bakeRequest: BakeRequest) {
        val bakeDb = BakeDb.newBakeFrom(user, bakeRequest)

        bakeDao.createNewBake(bakeDb)
    }

    fun getBakeList(user: String): List<Bake> {
        val out = bakeDao.getBakeList(user)
        val outMap = out.map { Bake.from(it) }
        return outMap
    }

    fun saveBake(user: String, bakeRequest: BakeRequest): BakeRequest {
        val bakeDb = BakeDb.newBakeFrom(user, bakeRequest)

        bakeDao.updateBake(user, bakeDb)
        return bakeRequest
    }

    fun getBake(user: String, bakeId: String): Bake {
        val bakeDb = bakeDao.getBake(user, bakeId.addPrefix(SortKeyPrefixes.BakeSortKey))
        return Bake.from(bakeDb)
    }

}
