/*
 * Copyright 2018 Andrew Giang
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.andrewgiang.homecontrol.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andrewgiang.homecontrol.DispatchProvider
import com.andrewgiang.homecontrol.api.AuthManager
import com.andrewgiang.homecontrol.data.database.model.Action
import com.andrewgiang.homecontrol.data.database.model.Data
import com.andrewgiang.homecontrol.data.model.AppAction
import com.andrewgiang.homecontrol.data.repo.ActionRepo
import com.andrewgiang.homecontrol.ui.Nav
import com.andrewgiang.homecontrol.ui.controller.HomeControllerDirections
import com.andrewgiang.homecontrol.util.CombinedLiveData
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class HomeUiModel(
    val actionIds: List<Action> = emptyList(),
    val isLoading: Boolean = false
)

class HomeViewModel @Inject constructor(
    private val actionRepo: ActionRepo,
    authManager: AuthManager,
    dispatchProvider: DispatchProvider
) :
    ScopeViewModel(dispatchProvider) {

    private val viewState = MutableLiveData<HomeUiModel>()

    init {
        if (!authManager.isAuthenticated()) {
            navigationState.postValue(Nav.Direction(HomeControllerDirections.toSetupFragment()))
        }
    }

    fun getViewState(): LiveData<HomeUiModel> {
        return CombinedLiveData(
            actionRepo.actionData(),
            viewState
        ) { actionList, viewState ->
            return@CombinedLiveData HomeUiModel(
                actionList ?: emptyList(),
                viewState?.isLoading ?: false
            )
        }
    }

    fun onClick(action: Action) {
        when (action) {
            is AppAction -> {
                handleAppAction(action)
            }
            else -> {
                invokeApiAction(action.data)
            }
        }
    }

    private fun handleAppAction(action: AppAction) {
        when (action) {
            is AppAction.AddAction -> {
                navigationState.postValue(Nav.Direction(HomeControllerDirections.toAddActionController()))
            }
        }
    }

    private fun invokeApiAction(data: Data) = launch {
        if (data is Data.ServiceData) {
            try {
                viewState.postValue(HomeUiModel(isLoading = true))
                val updatedStatus = actionRepo.invokeService(data.entityId, data.domain, data.service)
                Timber.d(updatedStatus.toString())
                viewState.postValue(HomeUiModel(isLoading = false))
            } catch (e: Exception) {
                Timber.d(e)
            }
        }
    }

    fun onShortcutClick(actionId: Long?) = launch {
        actionId?.let { id ->
            actionRepo.getAction(id)
        }?.let { action ->
            invokeApiAction(action.data)
        }
    }

    fun onDelete(action: Action) = launch {
        actionRepo.removeAction(action)
    }
}