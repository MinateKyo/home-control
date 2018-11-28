package com.andrewgiang.homecontrol.data.repo

import androidx.lifecycle.LiveData
import com.andrewgiang.homecontrol.DispatchProvider
import com.andrewgiang.homecontrol.api.ApiHolder
import com.andrewgiang.homecontrol.data.database.dao.EntityDao
import com.andrewgiang.homecontrol.data.database.model.Entity
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EntityRepo @Inject constructor(
    private val entityDao: EntityDao,
    private val apiHolder: ApiHolder,
    private val dispatchProvider: DispatchProvider
) {

    fun observeEntities(): LiveData<List<Entity>> {
        return entityDao.getEntities()
    }

    suspend fun refreshStates(): List<Long> {
        val entityResponse = apiHolder.api.getStates().await()
        return withContext(dispatchProvider.io) {
            entityDao.insert(entityResponse
                .map { it ->
                    Entity(it.entity_id, it.state, it.attributes)
                })
        }
    }
}