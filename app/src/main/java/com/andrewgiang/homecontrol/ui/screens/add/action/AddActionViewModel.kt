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

package com.andrewgiang.homecontrol.ui.screens.add.action

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andrewgiang.homecontrol.DispatchProvider
import com.andrewgiang.homecontrol.data.database.model.Action
import com.andrewgiang.homecontrol.data.database.model.Data
import com.andrewgiang.homecontrol.data.database.model.Entity
import com.andrewgiang.homecontrol.data.model.Icon
import com.andrewgiang.homecontrol.data.repo.ActionRepo
import com.andrewgiang.homecontrol.data.repo.EntityRepo
import com.andrewgiang.homecontrol.ui.ScopeViewModel
import kotlinx.coroutines.launch
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder
import javax.inject.Inject

class AddActionViewModel @Inject constructor(
    val actionRepo: ActionRepo,
    val entityRepo: EntityRepo,
    val dispatchProvider: DispatchProvider
) : ScopeViewModel(dispatchProvider) {

    private val viewState = MutableLiveData<ViewState>().apply { postValue(ViewState()) }
    private val entities = MutableLiveData<List<Entity>>()

    fun loadServices() = launch {
        viewState.postValue(
            ViewState(listDomainService = actionRepo.getDomainServiceList())
        )
    }

    fun getViewState(): LiveData<ViewState> {
        return viewState
    }

    fun addAction(
        domainService: String,
        entityId: List<String>,
        displayName: String,
        isShortcut: Boolean
    ) = launch {
        val split = domainService.split(".")
        val domain = split[0]
        val service = split[1]
        actionRepo.insertAction(
            Action(
                0,
                Data.ServiceData(entityId, domain, service),
                Icon(MaterialDrawableBuilder.IconValue.LIGHTBULB), // TODO customize icon
                displayName,
                isShortcut
            )
        )
        viewState.postValue(viewState.value?.copy(shouldFinish = true))
    }

    fun onItemSelected(domainService: String) = launch {
        val domain = domainService.split(".").firstOrEmpty()
        entities.postValue(entityRepo.getEntity(domain))
    }

    fun getEntities(): LiveData<List<Entity>> {
        return entities
    }
}

private fun List<String>.firstOrEmpty(): String {
    val firstOrNull = this.firstOrNull()
    return firstOrNull ?: ""
}

data class ViewState(
    val shouldFinish: Boolean = false,
    val listDomainService: List<String> = emptyList()
)