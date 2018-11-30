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

package com.andrewgiang.homecontrol.dagger.component

import androidx.lifecycle.ViewModelProvider
import com.andrewgiang.homecontrol.ui.controller.AddActionController
import com.andrewgiang.homecontrol.ui.controller.HomeController
import com.andrewgiang.homecontrol.ui.controller.SetupController
import dagger.Subcomponent

@Subcomponent(
    modules = [ControllerModule::class,
        ViewModelModule::class]
)
interface ControllerComponent {
    fun inject(fragment: SetupController)
    fun inject(controller: HomeController)
    fun inject(addActionController: AddActionController)
    fun getViewModelFactory(): ViewModelProvider.Factory
}
