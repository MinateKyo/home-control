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
import com.andrewgiang.homecontrol.data.database.model.Entity
import com.andrewgiang.homecontrol.data.model.DataHolder
import com.andrewgiang.homecontrol.data.repo.ActionRepo
import com.andrewgiang.homecontrol.data.repo.EntityRepo
import com.andrewgiang.homecontrol.firstOrEmpty
import com.andrewgiang.homecontrol.ui.Nav
import com.andrewgiang.homecontrol.ui.controller.AddActionControllerDirections
import com.andrewgiang.homecontrol.util.AppError
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AddActionUiModel {
    object Loading : AddActionUiModel()
    data class Error(val appError: AppError) : AddActionUiModel()
    data class ServiceLoaded(val services: List<String>) : AddActionUiModel()
    data class EntitiesLoaded(val entities: List<Entity>, val shouldShowEntities: Boolean) : AddActionUiModel()
}

class AddActionViewModel @Inject constructor(
    val actionRepo: ActionRepo,
    val entityRepo: EntityRepo,
    val dispatchProvider: DispatchProvider
) : ScopeViewModel(dispatchProvider) {

    private val ui = MutableLiveData<AddActionUiModel>()
    val checkedSet = mutableSetOf<Entity>()

    fun getUiState(): LiveData<AddActionUiModel> {
        return ui
    }

    fun onBind() = launch {
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
                AddActionControllerDirections.toIconEdit(
                    DataHolder(
                        selectedEntities,
                        domainService
                    )
                )
            )
        )
    }

    fun onItemSelected(domainService: String) = launch {
        val domain = domainService.split(".").firstOrEmpty()
        val entity = entityRepo.getEntity(domain)
        ui.postValue(
            AddActionUiModel.EntitiesLoaded(
                entity,
                shouldShowEntities = !entity.isNullOrEmpty()
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
}