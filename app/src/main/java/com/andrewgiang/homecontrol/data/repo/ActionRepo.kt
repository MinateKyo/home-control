package com.andrewgiang.homecontrol.data.repo

import androidx.lifecycle.LiveData
import com.andrewgiang.homecontrol.DispatchProvider
import com.andrewgiang.homecontrol.api.ApiHolder
import com.andrewgiang.homecontrol.data.database.dao.ActionDao
import com.andrewgiang.homecontrol.data.database.model.Action
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ActionRepo @Inject constructor(
    private val actionDao: ActionDao,
    private val apiHolder: ApiHolder,
    private val dispatchProvider: DispatchProvider
) {

    suspend fun getDomainServiceList(): List<String> {
        val serviceList = apiHolder.api.getServices().await()
        val serviceDomainList = ArrayList<String>()
        serviceList.forEach { entry ->
            entry.domain
            entry.services.keys.forEach { key ->
                serviceDomainList.add("${entry.domain}.$key")
            }
        }
        return serviceDomainList
    }

    fun actionData(): LiveData<List<Action>> {
        return actionDao.getActions()
    }

    suspend fun insertAction(action: Action) {
        withContext(dispatchProvider.io) {
            actionDao.insertAction(action)
        }
    }
}