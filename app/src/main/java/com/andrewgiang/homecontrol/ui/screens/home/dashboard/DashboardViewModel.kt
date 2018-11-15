package com.andrewgiang.homecontrol.ui.screens.home.dashboard

import androidx.lifecycle.LiveData
import com.andrewgiang.homecontrol.DispatchProvider
import com.andrewgiang.homecontrol.data.EntityRepository
import com.andrewgiang.homecontrol.data.database.entity.Entity
import com.andrewgiang.homecontrol.ui.ScopeViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    dispatchProvider: DispatchProvider,
    val entityRepository: EntityRepository
) :
    ScopeViewModel(dispatchProvider) {


    fun getEntities(): LiveData<List<Entity>> {
        return entityRepository.observeEntities()
    }


    fun refreshData() = launch {
        entityRepository.refreshStates()
    }


}