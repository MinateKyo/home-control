package com.andrewgiang.homecontrol.ui.screens.home.dashboard

import androidx.lifecycle.LiveData
import com.andrewgiang.homecontrol.DispatchProvider
import com.andrewgiang.homecontrol.data.database.model.Entity
import com.andrewgiang.homecontrol.data.repo.EntityRepo
import com.andrewgiang.homecontrol.ui.ScopeViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    dispatchProvider: DispatchProvider,
    val entityRepo: EntityRepo
) :
    ScopeViewModel(dispatchProvider) {

    fun getEntities(): LiveData<List<Entity>> {
        return entityRepo.observeEntities()
    }

    fun refreshData() = launch {
        entityRepo.refreshStates()
    }
}