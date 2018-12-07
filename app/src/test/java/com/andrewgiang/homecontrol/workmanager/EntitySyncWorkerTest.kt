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

import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.andrewgiang.homecontrol.App
import com.andrewgiang.homecontrol.api.AuthManager
import com.andrewgiang.homecontrol.data.repo.EntityRepo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class EntitySyncWorkerTest {

    val mockContext: App = mockk(relaxed = true)
    val params: WorkerParameters = mockk()
    val subject = EntitySyncWorker(mockContext, params)

    private val mockAuthManager: AuthManager = mockk()
    private val mockEntityRepo: EntityRepo = mockk()

    @Before
    fun setUp() {
        subject.authManager = mockAuthManager
        subject.entityRepo = mockEntityRepo
    }

    @Test
    fun `when not authenticated doWork will return success without calling refresh states`() {
        every { mockAuthManager.isAuthenticated() } returns false

        assertEquals(
            ListenableWorker.Result.SUCCESS, subject.doWork()
        )

        coVerify(inverse = true) { mockEntityRepo.refreshStates() }
    }

    @Test
    fun `when refresh state fails return retry`() {
        every { mockAuthManager.isAuthenticated() } returns true
        coEvery { mockEntityRepo.refreshStates() } throws Exception("something failed")

        assertEquals(
            ListenableWorker.Result.RETRY, subject.doWork()
        )

        coVerify { mockEntityRepo.refreshStates() }
    }

    @Test
    fun `when authenticated doWork will refresh entity states`() {
        every { mockAuthManager.isAuthenticated() } returns true
        coEvery { mockEntityRepo.refreshStates() } returns listOf()

        assertEquals(
            ListenableWorker.Result.SUCCESS, subject.doWork()
        )

        coVerify { mockEntityRepo.refreshStates() }
    }
}