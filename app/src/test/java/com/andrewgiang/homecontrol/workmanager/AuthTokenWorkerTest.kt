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
import com.andrewgiang.assistantsdk.Api
import com.andrewgiang.assistantsdk.response.AuthToken
import com.andrewgiang.homecontrol.App
import com.andrewgiang.homecontrol.api.ApiHolder
import com.andrewgiang.homecontrol.api.AuthManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AuthTokenWorkerTest {
    val mockContext: App = mockk(relaxed = true)
    val params: WorkerParameters = mockk()
    val subject = AuthTokenWorker(mockContext, params)

    private val mockAuthManager: AuthManager = mockk()

    private val mockApiHolder: ApiHolder = mockk()

    private val mockApi: Api = mockk()

    @Before
    fun setUp() {
        subject.authManager = mockAuthManager
        subject.apiHolder = mockApiHolder
        every { mockApiHolder.api } returns mockApi
    }

    @Test
    fun `when authenticated attempt to refresh auth token`() {
        every { mockAuthManager.isAuthenticated() } returns true
        val currentToken = AuthToken("current", 10, "refresh", "bearer")
        val newToken = AuthToken("new", 10, "refresh", "bearer")

        every { mockAuthManager.authToken } returns currentToken
        coEvery { mockApi.reauth(eq(currentToken)).await() } returns newToken

        assertEquals(ListenableWorker.Result.SUCCESS, subject.doWork())

        coVerify { mockApi.reauth(eq(currentToken)).await() }
        verify { mockAuthManager.updateAuthToken(eq(newToken)) }
    }

    @Test
    fun `when authenticated but somehow token is null return failure`() {
        every { mockAuthManager.isAuthenticated() } returns true
        val currentToken = null

        every { mockAuthManager.authToken } returns currentToken

        assertEquals(ListenableWorker.Result.FAILURE, subject.doWork())

        coVerify(inverse = true) { mockApi.reauth(any()).await() }
        verify(inverse = true) { mockAuthManager.updateAuthToken(any()) }
    }

    @Test
    fun `when not authenticated do nothing and return success`() {
        every { mockAuthManager.isAuthenticated() } returns false

        assertEquals(ListenableWorker.Result.SUCCESS, subject.doWork())

        coVerify(inverse = true) { mockApi.reauth(any()).await() }
        verify(inverse = true) { mockAuthManager.updateAuthToken(any()) }
    }
}