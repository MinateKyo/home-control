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

import com.andrewgiang.homecontrol.ui.screens.add.action.AddActionFragment
import com.andrewgiang.homecontrol.ui.screens.home.HomeFragment
import com.andrewgiang.homecontrol.ui.screens.home.dashboard.DashboardFragment
import com.andrewgiang.homecontrol.ui.screens.setup.UrlSetupFragment
import dagger.Subcomponent

@Subcomponent(modules = [ControllerModule::class, ViewModelModule::class])
interface ControllerComponent {
    fun inject(fragment: UrlSetupFragment)
    fun inject(fragment: HomeFragment)
    fun inject(fragment: DashboardFragment)
    fun inject(addActionFragment: AddActionFragment)
}
