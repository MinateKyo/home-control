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
import com.andrewgiang.homecontrol.data.database.model.Action
import com.andrewgiang.homecontrol.data.database.model.Entity
import com.andrewgiang.homecontrol.data.model.DataHolder
import com.andrewgiang.homecontrol.data.repo.ActionRepo
import com.andrewgiang.homecontrol.data.repo.EntityRepo
import com.andrewgiang.homecontrol.firstOrEmpty
import com.andrewgiang.homecontrol.ui.Nav
import com.andrewgiang.homecontrol.ui.controller.ActionControllerDirections
import com.andrewgiang.homecontrol.util.AppError
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AddActionUiModel {
    object Loading : AddActionUiModel()
    data class Error(val appError: AppError) : AddActionUiModel()
    data class ServiceLoaded(val services: List<String>) : AddActionUiModel()
    data class EntitiesLoaded(val entities: List<Entity>, val shouldShowEntities: Boolean) : AddActionUiModel()
    data class EditMode(
        val services: List<String>,
        val entities: List<Entity>,
        val shouldShowEntities: Boolean,
        val selectedServiceIndex: Int,
        val selectedEntityId: List<String>
    ) : AddActionUiModel()
}

class ActionViewModel @Inject constructor(
    val actionRepo: ActionRepo,
    val entityRepo: EntityRepo,
    val dispatchProvider: DispatchProvider
) : ScopeViewModel(dispatchProvider) {
    private var editActionId: Long = -1L
    private val ui = MutableLiveData<AddActionUiModel>()
    val checkedSet = mutableSetOf<Entity>()

    fun getUiState(): LiveData<AddActionUiModel> {
        return ui
    }

    private fun freshLaunch() = launch {
        ui.apply {
            postValue(AddActionUiModel.Loading)
            try {
                postValue(AddActionUiModel.ServiceLoaded(actionRepo.getDomainServiceList()))
            } catch (e: Throwable) {
                postValue(AddActionUiModel.Error(AppError.from(e)))
            }
        }
    }

    fun onNextButtonClicked(domainService: String) {
        val selectedEntities = checkedSet.toList()
        navigationState.postValue(
            Nav.Direction(
                ActionControllerDirections.toIconEdit(
                    DataHolder(
                        editActionId,
                        selectedEntities,
                        domainService
                    )
                )
            )
        )
    }

    fun onDomainServiceSelected(domainService: String) = launch {
        val domain = domainService.split(".").firstOrEmpty()
        val entity = entityRepo.getEntity(domain)
        ui.postValue(
            AddActionUiModel.EntitiesLoaded(
                entity,
                shouldShowEntities = entity.isNotEmpty()
            )
        )
    }

    fun onChipChecked(entity: Entity, checked: Boolean) {
        if (checked) {
            checkedSet.add(entity)
        } else {
            checkedSet.remove(entity)
        }
    }

    fun clearChips() {
        checkedSet.clear()
    }

    /**
     * if actionId is found in the db enter edit mode, otherwise do a fresh launch
     */
    fun load(actionId: Long) = launch {
        editActionId = actionId
        val action = actionRepo.getAction(actionId)
        ui.apply {
            if (action != null) {
                postValue(AddActionUiModel.Loading)
                try {
                    postValue(createEditModeUiModel(action))
                } catch (e: Throwable) {
                    postValue(AddActionUiModel.Error(AppError.from(e)))
                }
            } else {
                freshLaunch()
            }
        }
    }

    private suspend fun createEditModeUiModel(action: Action): AddActionUiModel.EditMode {
        val entities = entityRepo.getEntity(action.data.domain)
        val domainServiceList = actionRepo.getDomainServiceList()
        val selectedDomainIndex =
            domainServiceList.indexOfFirst { domainService -> domainService == action.data.getDomainService() }
        return AddActionUiModel.EditMode(
            domainServiceList,
            entities,
            entities.isNotEmpty(),
            selectedDomainIndex,
            action.data.entityId
        )
    }
}