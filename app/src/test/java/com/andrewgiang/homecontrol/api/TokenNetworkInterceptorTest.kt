package com.andrewgiang.homecontrol.api

import com.andrewgiang.assistantsdk.response.AuthToken
import com.andrewgiang.homecontrol.addAuthHeader
import io.mockk.*
import okhttp3.Interceptor
import okhttp3.Request
import org.junit.Test

class TokenNetworkInterceptorTest {

    val authManager: AuthManager = mockk()

    val subject: TokenNetworkInterceptor = TokenNetworkInterceptor(authManager)

    @Test
    fun test_will_add_auth_token_to_request_if_user_is_authorized() {

        val concreteBuilder = Request.Builder().url("https://google.com")

        val chain = mockk<Interceptor.Chain>()
        val spyRequest = spyk(concreteBuilder.build())
        val builderSpy = spyk<Request.Builder>(concreteBuilder)
        val authToken = mockk<AuthToken>()
        val expectedToken = "1234"

        every { authManager.authToken } returns authToken
        every { spyRequest.newBuilder() } returns builderSpy
        every { authManager.isAuthenticated() } returns true
        every { chain.request() } returns spyRequest
        every { chain.proceed(any()) } returns mockk()
        every { authToken.access_token } returns expectedToken


        subject.intercept(chain)

        verifyOrder {
            spyRequest.newBuilder()
            authToken.access_token
            builderSpy.addAuthHeader(expectedToken)
            builderSpy.build()
        }
    }

    @Test
    fun test_will_proceed_with_original_request_if_not_authenticated() {

        val chain = mockk<Interceptor.Chain>()
        val originalRequest = mockk<Request>()

        every { authManager.isAuthenticated() } returns false
        every { chain.request() } returns originalRequest
        every { chain.proceed(eq(originalRequest)) } returns mockk()

        subject.intercept(chain)

        verify {
            chain.proceed(originalRequest)
        }
    }

}