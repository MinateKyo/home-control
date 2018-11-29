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

package com.andrewgiang.homecontrol.ui.screens

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.andrewgiang.assistantsdk.Api
import com.andrewgiang.assistantsdk.response.AuthToken
import com.andrewgiang.homecontrol.api.ApiHolder
import com.andrewgiang.homecontrol.api.AuthManager
import com.andrewgiang.homecontrol.ui.IntentCreator
import com.andrewgiang.homecontrol.ui.screens.setup.AuthState
import com.andrewgiang.homecontrol.ui.screens.setup.UrlState
import com.andrewgiang.homecontrol.ui.screens.setup.UrlViewModel
import com.andrewgiang.homecontrol.ui.testDispatchProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
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

class UrlViewModelTest {

    val intentCreator: IntentCreator = mockk()
    val mockHolder: ApiHolder = mockk()
    val api: Api = mockk()
    var authManager: AuthManager = mockk()

    val subject: UrlViewModel =
        UrlViewModel(intentCreator, mockHolder, authManager, testDispatchProvider())

    @Before
    fun setUp() {
        every { mockHolder.api } returns api
    }

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun onNext_with_valid_url() {
        val urlText = "https://validurl.com"
        val hostUrl = HttpUrl.parse(urlText)

        subject.onNextClick(urlText)

        val urlState = subject.getData().value

        verify {
            authManager.setHost(eq(hostUrl.toString()))
        }
        verify {
            intentCreator.sendAuthorizeIntent(eq(hostUrl!!))
        }
        assertNotNull(urlState)
        assertEquals(UrlState(isLoading = true), urlState)
    }

    @Test
    fun onNext_with_invalid_url() {
        val urlText = "invalidUrl"
        subject.onNextClick(urlText)

        val urlState = subject.getData().value

        assertNotNull(urlState)
        assertEquals(
            UrlState(
                isLoading = false,
                errorMessage = "Invalid Url : $urlText"
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
        assertEquals(UrlState(isLoading = false, errorMessage = "Unable to authenticate"), subject.getData().value)
    }

    @Test
    fun onAppLinkRedirect_with_authorization_code_success() {
        val mockCall: Deferred<AuthToken> = mockk(relaxUnitFun = true)
        val code = "1234"
        val token = AuthToken("1234", 3, "3412", "bearer")

        every { api.initialAuth(eq(code)) } returns mockCall
        coEvery { mockCall.await() } returns token

        subject.onAppLinkRedirect(code)

        coVerify { api.initialAuth(code) }
        verify { authManager.updateAuthToken(token) }
        assertEquals(
            UrlState(
                isLoading = false,
                authState = AuthState.AUTHENTICATED
            ), subject.getData().value
        )
    }
}