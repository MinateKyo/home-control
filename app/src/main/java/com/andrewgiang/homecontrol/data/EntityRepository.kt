package com.andrewgiang.homecontrol.data

import androidx.lifecycle.LiveData
import com.andrewgiang.homecontrol.DispatchProvider
import com.andrewgiang.homecontrol.api.ApiHolder
import com.andrewgiang.homecontrol.data.database.entity.Entity
import com.andrewgiang.homecontrol.data.database.entity.EntityDao
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EntityRepository @Inject constructor(
    val entityDao: EntityDao,
    val apiHolder: ApiHolder,
    val dispatchProvider: DispatchProvider
) {


    fun observeEntities(): LiveData<List<Entity>> {
        return entityDao.getEntities()
    }

    suspend fun refreshStates() {
        val entityResponse = apiHolder.api.getStates().await()

        withContext(dispatchProvider.io) {
            entityDao
                .insert(entityResponse
                    .map { it ->
                        Entity(it.entity_id, it.state, it.attributes)
                    })
        }

    }

}