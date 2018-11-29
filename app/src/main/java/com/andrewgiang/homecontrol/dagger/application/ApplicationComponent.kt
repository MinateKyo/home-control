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

package com.andrewgiang.homecontrol.dagger.application

import com.andrewgiang.homecontrol.App
import com.andrewgiang.homecontrol.dagger.component.ApiModule
import com.andrewgiang.homecontrol.dagger.component.ControllerComponent
import com.andrewgiang.homecontrol.dagger.component.ControllerModule
import com.andrewgiang.homecontrol.workmanager.AuthTokenWorker
import com.andrewgiang.homecontrol.workmanager.EntitySyncWorker
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        ApplicationModule::class,
        SecurityModule::class,
        ApiModule::class,
        DatabaseModule::class
    ]
)
interface ApplicationComponent {

    fun inject(app: App)
    fun inject(tokenWorker: AuthTokenWorker)
    fun inject(entitySyncWorker: EntitySyncWorker)
    fun create(controllerModule: ControllerModule): ControllerComponent
}