package com.andrewgiang.homecontrol.ui.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andrewgiang.homecontrol.DispatchProvider
import com.andrewgiang.homecontrol.SingleLiveEvent
import com.andrewgiang.homecontrol.api.ApiHolder
import com.andrewgiang.homecontrol.api.AuthManager
import com.andrewgiang.homecontrol.data.model.Action
import com.andrewgiang.homecontrol.data.model.AppAction
import com.andrewgiang.homecontrol.ui.ScopeViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


class HomeViewModel @Inject constructor(
    private val holder: ApiHolder,
    authManager: AuthManager,
    dispatchProvider: DispatchProvider
) :
    ScopeViewModel(dispatchProvider) {

    private val data = MutableLiveData<HomeState>()

    private val appAction = SingleLiveEvent<AppAction>()

    init {
        data.postValue(HomeState(isAuthenticated = authManager.isAuthenticated()))
    }

    fun getData(): LiveData<HomeState> {
        return data
    }

    fun getAppActions(): LiveData<AppAction> {
        return appAction
    }

    fun onClick(action: Action) {
        when (action.service) {
            is AppAction -> {
                appAction.postValue(action.service)
            }
            else -> {
                invokeApiAction(action)
            }
        }
    }

    private fun invokeApiAction(action: Action) = launch {
        try {
            val list = holder.api.service(action.entityId, action.service).await()
            Timber.d(list.toString())
        } catch (e: Exception) {
            Timber.d(e)
        }
    }

}