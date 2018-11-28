package com.andrewgiang.homecontrol.data.repo

import androidx.lifecycle.LiveData
import com.andrewgiang.homecontrol.DispatchProvider
import com.andrewgiang.homecontrol.api.ApiHolder
import com.andrewgiang.homecontrol.data.database.dao.ActionDao
import com.andrewgiang.homecontrol.data.database.model.Action
import javax.inject.Inject

class ActionRepo @Inject constructor(
    private val actionDao: ActionDao,
    private val apiHolder: ApiHolder,
    private val dispatchProvider: DispatchProvider
) {

    fun actionData(): LiveData<List<Action>> {
        return actionDao.getActions()
    }
}