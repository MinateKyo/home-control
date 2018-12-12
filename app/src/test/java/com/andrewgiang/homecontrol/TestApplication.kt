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

package com.andrewgiang.homecontrol

import androidx.work.WorkManager
import com.andrewgiang.homecontrol.api.AuthManager
import com.andrewgiang.homecontrol.dagger.application.ApplicationModule
import com.andrewgiang.homecontrol.dagger.application.DaggerApplicationComponent
import com.andrewgiang.homecontrol.data.AuthPrefs
import org.mockito.Mockito.mock

class TestApplication : App() {
    val testAppModule = TestAppModule(this)

    override val applicationComponent = DaggerApplicationComponent.builder()
        .applicationModule(
            testAppModule
        ).build()

    override fun onCreate() {}
}

class TestAppModule(testApplication: TestApplication) : ApplicationModule(testApplication) {
    val mockAuthManager: AuthManager = mock(AuthManager::class.java)
    val mockWorkManager: WorkManager = mock(WorkManager::class.java)

    override fun authManager(authPrefs: AuthPrefs): AuthManager {
        return mockAuthManager
    }

    override fun workManager(): WorkManager {
        return mockWorkManager
    }
}