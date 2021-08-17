package com.kneadybread.service

import com.google.inject.Inject
import com.kneadybread.dao.BakeDao
import com.kneadybread.dao.UserDao
import com.kneadybread.domain.Bake
import com.kneadybread.domain.request.NewBakeRequest
import com.kneadybread.util.SortKeyPrefixes

class BakeService @Inject constructor(private val userDao: UserDao, private val bakeDao: BakeDao) {



    fun saveNewBake(user: String, bakeRequest: NewBakeRequest) {
        val bake = Bake.from(user, bakeRequest)

        bakeDao.createNewBake(bake)
    }

    fun getBakeList(user: String): List<Bake> {
        return bakeDao.getBakeList(user)
    }

}
