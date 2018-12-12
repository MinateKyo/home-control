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
import com.andrewgiang.homecontrol.data.repo.ActionRepo
import com.andrewgiang.homecontrol.ui.Nav
import com.andrewgiang.homecontrol.ui.controller.HomeControllerDirections
import com.andrewgiang.homecontrol.ui.view.Loading
import com.andrewgiang.homecontrol.util.AppError
import com.andrewgiang.homecontrol.util.CombinedLiveData
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class HomeUiModel(
    val actionIds: List<Action> = emptyList(),
    val loading: Loading = Loading()
)

class HomeViewModel @Inject constructor(
    private val actionRepo: ActionRepo,
    authManager: AuthManager,
    dispatchProvider: DispatchProvider
) : ScopeViewModel(dispatchProvider) {

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
                viewState?.loading ?: Loading()
            )
        }
    }

    fun onClick(action: Action) {
        invokeAction(action)
    }

    private fun invokeAction(action: Action) = launch {
        val data: Data = action.data
        try {
            viewState.postValue(HomeUiModel(loading = Loading(true, message = action.name)))
            val updatedStatus = actionRepo.invokeService(data.entityId, data.domain, data.service)
            Timber.d(updatedStatus.toString())
            viewState.postValue(HomeUiModel(loading = Loading(false)))
        } catch (e: Throwable) {
            viewState.postValue(HomeUiModel(loading = Loading(isLoading = false, appError = AppError.from(e))))
        }
    }

    fun onShortcutClick(actionId: Long?) = launch {
        actionId?.let { id ->
            actionRepo.getAction(id)
        }?.let { action ->
            invokeAction(action)
        }
    }

    fun onDelete(action: Action) = launch {
        actionRepo.removeAction(action)
    }

    fun onAddActionClick() {
        navigationState.postValue(Nav.Direction(HomeControllerDirections.toAddActionController()))
    }
}