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

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import org.junit.Test

class PeriodicWorkManagerTest {

    val mockWorkRequestFactory = mockk<WorkRequestFactory>(relaxed = true)
    val mockWorkManager = mockk<WorkManager>()

    val subject: PeriodicWorkManager = PeriodicWorkManager(mockWorkManager, mockWorkRequestFactory)

    @Test
    fun `will enqueue all work from work request factory`() {
        val periodicWorkRequest = mockk<PeriodicWorkRequest>()
        val listOfWork = listOf(
            "work" to periodicWorkRequest,
            "work2" to periodicWorkRequest
        )
        every { mockWorkRequestFactory.workRequests } returns listOfWork
        every { mockWorkManager.enqueueUniquePeriodicWork(any(), any(), any()) } returns mockk()

        subject.enqueueWork()

        verifyAll {
            mockWorkManager.enqueueUniquePeriodicWork(
                eq("work"),
                ExistingPeriodicWorkPolicy.KEEP,
                eq(periodicWorkRequest)
            )
            mockWorkManager.enqueueUniquePeriodicWork(
                eq("work2"),
                ExistingPeriodicWorkPolicy.KEEP,
                eq(periodicWorkRequest)
            )
        }
    }
}