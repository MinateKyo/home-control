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

@file:Suppress("DeferredResultUnused")
package com.andrewgiang.homecontrol.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.andrewgiang.assistantsdk.Api
import com.andrewgiang.assistantsdk.response.AuthToken
import com.andrewgiang.homecontrol.api.ApiHolder
import com.andrewgiang.homecontrol.api.AuthManager
import com.andrewgiang.homecontrol.data.repo.EntityRepo
import com.andrewgiang.homecontrol.testDispatchProvider
import com.andrewgiang.homecontrol.util.IntentCreator
import com.andrewgiang.homecontrol.viewmodel.SetupViewModel.Companion.DEFAULT_PORT
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Deferred
import okhttp3.HttpUrl
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.io.IOException

class SetupViewModelTest {

    private val intentCreator: IntentCreator = mockk()
    private val mockHolder: ApiHolder = mockk()
    private val api: Api = mockk()
    val mockEntityRepo: EntityRepo = mockk()
    private var authManager: AuthManager = mockk()

    val subject: SetupViewModel =
        SetupViewModel(
            intentCreator,
            mockHolder,
            authManager,
            mockEntityRepo,
            testDispatchProvider()
        )

    @Before
    fun setUp() {
        every { mockHolder.create(any()) } returns api
        every { mockHolder.api } returns api
        coEvery { api.checkApi() } returns true
    }

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun `onFinish click with api check throws error`() {
        val host = "validurl.com"
        coEvery { api.checkApi() } throws IOException()
        val expectedUrl = HttpUrl.Builder()
            .scheme("http")
            .host(host)
            .port(DEFAULT_PORT)
            .build()
        subject.onFinishClicked(false, host, "")

        verifyApiFailure(expectedUrl)
    }

    @Test
    fun `onFinish click with api check false will throw unable to connect state`() {
        val host = "validurl.com"
        coEvery { api.checkApi() } returns false
        val expectedUrl = HttpUrl.Builder()
            .scheme("http")
            .host(host)
            .port(DEFAULT_PORT)
            .build()
        subject.onFinishClicked(false, host, "")

        verifyApiFailure(expectedUrl)
    }

    @Test
    fun `onFinish with https false`() {
        val host = "validurl.com"
        val expectedUrl = HttpUrl.Builder()
            .scheme("http")
            .host(host)
            .port(DEFAULT_PORT)
            .build()
        subject.onFinishClicked(false, host, "")
        verifyValid(expectedUrl)
    }

    @Test
    fun `onFinish with with custom set port`() {
        val host = "validurl.com"
        val expectedUrl = HttpUrl.Builder()
            .scheme("https")
            .host(host)
            .port(1234)
            .build()
        subject.onFinishClicked(true, host, "1234")
        verifyValid(expectedUrl)
    }

    @Test
    fun `onFinish with valid url`() {
        val host = "validurl.com"
        val expectedUrl = HttpUrl.Builder()
            .scheme("https")
            .host(host)
            .port(DEFAULT_PORT)
            .build()

        subject.onFinishClicked(true, host, "")
        verifyValid(expectedUrl)
    }

    @Test
    fun onNext_with_invalid_url() {
        val urlText = "fiow@31#"
        subject.onFinishClicked(true, urlText, "")

        val urlState = subject.getData().value

        assertNotNull(urlState)
        assertEquals(
            SetupUiModel(
                isLoading = false,
                errorMessage = "Invalid Url"
            ), urlState
        )
    }

    @Test
    fun onAppLinkRedirect_null_authorization_code() {
        subject.onAppLinkRedirect(null)
        verify(inverse = true) {
            api.initialAuth(any())
        }
    }

    @Test
    fun onAppLinkRedirect_with_authorization_code_failure() {
        val mockCall: Deferred<AuthToken> = mockk()

        val code = "1234"

        every { api.initialAuth(eq(code)) } returns mockCall
        coEvery { mockCall.await() } throws IllegalArgumentException()

        subject.onAppLinkRedirect(code)

        coVerify { api.initialAuth(code) }
        assertEquals(
            SetupUiModel(
                isLoading = false,
                errorMessage = "Unable to authenticate"
            ), subject.getData().value
        )
    }

    @Test
    fun onAppLinkRedirect_with_authorization_code_success() {
        val mockCall: Deferred<AuthToken> = mockk(relaxUnitFun = true)
        val code = "1234"
        val token = AuthToken("1234", 3, "3412", "bearer")

        every { api.initialAuth(eq(code)) } returns mockCall
        coEvery { mockCall.await() } returns token

        coEvery { mockEntityRepo.refreshStates() } just Runs
        subject.onAppLinkRedirect(code)

        coVerify { api.initialAuth(code) }
        verify { authManager.updateAuthToken(token) }
        assertEquals(
            SetupUiModel(
                isLoading = false,
                authState = AuthState.AUTHENTICATED
            ), subject.getData().value
        )
    }

    private fun verifyValid(expectedUrl: HttpUrl) {
        verify {
            authManager.setHost(eq(expectedUrl.toString()))
        }
        verify {
            intentCreator.sendAuthorizeIntent(eq(expectedUrl))
        }
    }

    private fun verifyApiFailure(expectedUrl: HttpUrl) {
        verify(inverse = true) {
            intentCreator.sendAuthorizeIntent(eq(expectedUrl))
        }
        assertEquals(
            SetupUiModel(errorMessage = "Unable to connect to $expectedUrl"),
            subject.getData().value
        )
    }
}