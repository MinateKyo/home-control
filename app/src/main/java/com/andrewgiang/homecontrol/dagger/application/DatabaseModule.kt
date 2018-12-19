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

import android.app.Application
import androidx.room.Room
import com.andrewgiang.homecontrol.BuildConfig
import com.andrewgiang.homecontrol.data.database.AppDatabase
import com.andrewgiang.homecontrol.data.database.dao.ActionDao
import com.andrewgiang.homecontrol.data.database.dao.HomeDao
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {

    @ApplicationScope
    @Provides
    fun db(app: Application): AppDatabase {
        val builder = Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "app.db"
        )
        if (BuildConfig.DEBUG) {
            builder.fallbackToDestructiveMigration()
        }
        return builder.build()
    }

    @ApplicationScope
    @Provides
    fun entityDao(appDatabase: AppDatabase): HomeDao {
        return appDatabase.entityDao()
    }

    @ApplicationScope
    @Provides
    fun actionDao(appDatabase: AppDatabase): ActionDao {
        return appDatabase.actionDao()
    }
}