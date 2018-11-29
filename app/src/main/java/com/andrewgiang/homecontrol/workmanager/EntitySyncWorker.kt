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

package com.andrewgiang.homecontrol.workmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.andrewgiang.homecontrol.App
import com.andrewgiang.homecontrol.api.AuthManager
import com.andrewgiang.homecontrol.data.repo.EntityRepo
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class EntitySyncWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    @Inject
    lateinit var authManager: AuthManager
    @Inject
    lateinit var entityRepo: EntityRepo

    override fun doWork(): Result {
        (applicationContext as App).applicationComponent.inject(this)
        return if (!authManager.isAuthenticated()) {
            Result.SUCCESS
        } else {
            refreshState()
        }
    }

    private fun refreshState(): Result {
        return runBlocking {
            return@runBlocking try {
                entityRepo.refreshStates()
                Result.SUCCESS
            } catch (e: Exception) {
                Result.RETRY
            }
        }
    }
}