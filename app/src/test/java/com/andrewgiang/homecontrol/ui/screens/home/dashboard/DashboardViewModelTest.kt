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

package com.andrewgiang.homecontrol.ui.screens.home.dashboard

import androidx.lifecycle.LiveData
import com.andrewgiang.homecontrol.DispatchProvider
import com.andrewgiang.homecontrol.data.database.model.Entity
import com.andrewgiang.homecontrol.data.repo.EntityRepo
import com.andrewgiang.homecontrol.ui.testDispatchProvider
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class DashboardViewModelTest {

    val dispatchProvider: DispatchProvider = testDispatchProvider()

    val entityRepo: EntityRepo = mockk(relaxed = true)

    val subject: DashboardViewModel = DashboardViewModel(dispatchProvider, entityRepo)

    @Test
    fun test_refresh_data_will_invoke_repository_refresh() {
        subject.refreshData()
        coVerify {
            entityRepo.refreshStates()
        }
    }

    @Test
    fun test_repository_return_observed_live_data() {
        val liveData = mockk<LiveData<List<Entity>>>()
        every { entityRepo.observeEntities() } returns liveData
        assertEquals(subject.getEntities(), liveData)
    }
}