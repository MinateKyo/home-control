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

package com.andrewgiang.homecontrol.api

import com.andrewgiang.assistantsdk.Api
import com.andrewgiang.assistantsdk.response.AuthToken
import com.andrewgiang.homecontrol.addAuthHeader
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.Deferred
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class AuthTokenAuthenticatorTest {

    private val mockAuthManager: AuthManager = mockk()
    private val mockApi: Api = mockk()
    val subject = AuthTokenAuthenticator(mockAuthManager)

    init {
        AuthTokenAuthenticator.api = mockApi
    }

    private val mockRoute: Route = mockk()

    private val mockResponse: Response = mockk()

    private val realRequest = Request.Builder().url("https://someurl.com").build()

    private val spyRequest = spyk(realRequest)

    private val spyBuilder: Request.Builder = spyk(realRequest.newBuilder())

    @Before
    fun setUp() {
        every { mockResponse.request() } returns spyRequest
        every { spyRequest.newBuilder() } returns spyBuilder
    }

    @Test
    fun `attempt reauth if authenticated and has current token will update to new token`() {

        val currentToken = AuthToken("currentToken", 1, "abc", "bearer")
        val newToken = AuthToken("newToken", 1, "abc", "bearer")
        val mockDeferred = mockk<Deferred<AuthToken>>()

        every { mockAuthManager.authToken } returns currentToken
        coEvery { mockDeferred.await() } returns newToken
        coEvery { mockApi.reauth(eq(currentToken)) } returns mockDeferred

        subject.authenticate(mockRoute, mockResponse)

        verifyOrder {
            mockApi.reauth(eq(currentToken))
            mockAuthManager.updateAuthToken(eq(newToken))
            spyRequest.newBuilder()
            spyBuilder.addAuthHeader(newToken.access_token)
            spyBuilder.build()
        }
    }

    @Test
    fun `will not attempt reauth when current token is null and returns null`() {
        every { mockAuthManager.authToken } returns null

        assertNull(subject.authenticate(mockRoute, mockResponse))

        verify(inverse = true) {
            mockApi.reauth(any())
            mockAuthManager.updateAuthToken(any())
        }
    }
}