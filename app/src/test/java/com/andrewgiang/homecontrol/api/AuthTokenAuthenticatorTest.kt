package com.andrewgiang.homecontrol.api

import com.andrewgiang.assistantsdk.Api
import com.andrewgiang.assistantsdk.response.AuthToken
import com.andrewgiang.homecontrol.addAuthHeader
import io.mockk.*
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