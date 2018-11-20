package com.andrewgiang.homecontrol.ui.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andrewgiang.homecontrol.ActionShortcutManager
import com.andrewgiang.homecontrol.DispatchProvider
import com.andrewgiang.homecontrol.SingleLiveEvent
import com.andrewgiang.homecontrol.api.ApiHolder
import com.andrewgiang.homecontrol.api.AuthManager
import com.andrewgiang.homecontrol.data.model.Action
import com.andrewgiang.homecontrol.data.model.AppAction
import com.andrewgiang.homecontrol.data.model.Data
import com.andrewgiang.homecontrol.ui.ScopeViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


class HomeViewModel @Inject constructor(
    private val holder: ApiHolder,
    private val actionShortcutManager: ActionShortcutManager,
    private val authManager: AuthManager,
    dispatchProvider: DispatchProvider
) :
    ScopeViewModel(dispatchProvider) {

    private val viewState = MutableLiveData<HomeState>()

    private val appAction = SingleLiveEvent<AppAction>()

    init {
        viewState.postValue(HomeState(isAuthenticated = authManager.isAuthenticated()))
    }

    fun getViewState(): LiveData<HomeState> {
        return viewState
    }

    fun getAppActions(): LiveData<AppAction> {
        return appAction
    }

    fun onClick(action: Action) {
        when (action) {
            is AppAction -> {
                appAction.postValue(action)
            }
            else -> {
                if (action.data is Data.ServiceData) {
                    viewState.postValue(HomeState(isLoading = true, isAuthenticated = authManager.isAuthenticated()))
                    invokeApiAction(action.data)
                }
            }
        }
    }

    private fun invokeApiAction(data: Data.ServiceData) = launch {
        try {
            val list = holder.api.service(data.entityId, data.domain, data.service).await()
            Timber.d(list.toString())

            viewState.postValue(HomeState(isLoading = false, isAuthenticated = authManager.isAuthenticated()))
        } catch (e: Exception) {
            Timber.d(e)
        }
    }

    fun onShortcutClick(shortcutJsonData: String?) {
        if (shortcutJsonData != null) {
            val serviceAction = actionShortcutManager.fromJson(shortcutJsonData)
            serviceAction?.let {
                invokeApiAction(it)
            }
        }
    }

}