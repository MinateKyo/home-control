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
import com.andrewgiang.homecontrol.data.database.model.Data
import com.andrewgiang.homecontrol.data.model.ColorType
import com.andrewgiang.homecontrol.data.model.HomeIcon
import com.andrewgiang.homecontrol.data.repo.ActionRepo
import com.andrewgiang.homecontrol.ui.Nav
import com.andrewgiang.homecontrol.ui.controller.IconEditControllerDirections
import kotlinx.coroutines.launch
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder
import javax.inject.Inject

data class IconUiModel(
    val homeIcon: HomeIcon = HomeIcon(
        MaterialDrawableBuilder.IconValue.LIGHTBULB,
        colorType = ColorType.INT
    ),
    val displayName: String = "Preview"
)

class IconEditViewModel @Inject constructor(
    private val actionRepo: ActionRepo,
    dispatchProvider: DispatchProvider
) :
    ScopeViewModel(dispatchProvider) {

    private val ui = MutableLiveData<IconUiModel>()
        .apply {
            postValue(IconUiModel())
        }

    fun getUiModel(): LiveData<IconUiModel> {
        return ui
    }

    fun onAddButtonClicked(
        selectedEntities: List<String>,
        domainService: String,
        displayName: String,
        isShortcut: Boolean
    ) = launch {
        val split = domainService.split(".")
        val domain = split[0]
        val service = split[1]
        actionRepo.insertAction(
            Action(
                0,
                Data(selectedEntities, domain, service),
                ui.value!!.homeIcon,
                displayName,
                isShortcut
            )
        )
        navigationState.postValue(Nav.Direction(IconEditControllerDirections.toHome()))
    }

    fun onBackgroundColorSelected(color: Int) {
        postUi {
            it.copy(homeIcon = it.homeIcon.copy(backgroundColorInt = color))
        }
    }

    fun onIconColorSelected(color: Int) {
        postUi {
            it.copy(homeIcon = it.homeIcon.copy(iconColorInt = color))
        }
    }

    fun onTextChanged(displayName: String) {
        postUi {
            it.copy(displayName = displayName)
        }
    }

    private fun postUi(function: (IconUiModel) -> IconUiModel) {
        ui.value?.let {
            ui.postValue(function.invoke(it))
        }
    }

    fun onIconSelected(iconValue: MaterialDrawableBuilder.IconValue) {
        postUi {
            it.copy(homeIcon = it.homeIcon.copy(iconValue = iconValue))
        }
    }
}