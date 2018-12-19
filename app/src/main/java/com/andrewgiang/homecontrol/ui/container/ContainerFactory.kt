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

package com.andrewgiang.homecontrol.ui.container

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.andrewgiang.homecontrol.viewmodel.ActionViewModel
import com.andrewgiang.homecontrol.viewmodel.HomeViewModel
import com.andrewgiang.homecontrol.viewmodel.IconEditViewModel
import com.andrewgiang.homecontrol.viewmodel.SetupViewModel

class ContainerFactory(
    private val inflater: LayoutInflater,
    private val container: ViewGroup?,
    private val viewLifecycleOwner: LifecycleOwner,
    private val navController: NavController
) {
    fun create(actionViewModel: ActionViewModel): ActionViewContainer {
        return ActionViewContainer(
            inflater,
            container,
            viewLifecycleOwner,
            navController,
            actionViewModel
        )
    }

    fun create(homeViewModel: HomeViewModel): HomeViewContainer {
        return HomeViewContainer(
            inflater,
            container,
            viewLifecycleOwner,
            navController,
            homeViewModel
        )
    }

    fun create(iconEditViewModel: IconEditViewModel): IconEditContainer {
        return IconEditContainer(
            inflater,
            container,
            viewLifecycleOwner,
            navController,
            iconEditViewModel
        )
    }

    fun create(setupViewModel: SetupViewModel): SetupViewContainer {
        return SetupViewContainer(
            inflater,
            container,
            viewLifecycleOwner,
            navController,
            setupViewModel
        )
    }
}