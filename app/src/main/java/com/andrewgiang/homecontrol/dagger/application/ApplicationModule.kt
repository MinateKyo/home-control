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

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.ShortcutManager
import androidx.work.WorkManager
import com.andrewgiang.homecontrol.DispatchProvider
import com.andrewgiang.homecontrol.api.AuthManager
import com.andrewgiang.homecontrol.api.AuthManagerImpl
import com.andrewgiang.homecontrol.data.AuthPrefs
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers

@Module
open class ApplicationModule(val app: Application) {

    @ApplicationScope
    @Provides
    fun app(): Application {
        return app
    }

    @ApplicationScope
    @Provides
    open fun authManager(authPrefs: AuthPrefs): AuthManager {
        return AuthManagerImpl(authPrefs)
    }

    @ApplicationScope
    @Provides
    open fun workManager(): WorkManager {
        return WorkManager.getInstance()
    }

    @ApplicationScope
    @Provides
    fun moshi(): Moshi {
        return Moshi.Builder().build()
    }

    @SuppressLint("NewApi")
    @ApplicationScope
    @Provides
    fun shortcutManager(app: Application): ShortcutManager {
        return app.getSystemService(Context.SHORTCUT_SERVICE) as ShortcutManager
    }

    @ApplicationScope
    @Provides
    fun dispatchers(): DispatchProvider {
        return DispatchProvider(Dispatchers.IO, Dispatchers.Main)
    }
}