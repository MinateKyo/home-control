package com.andrewgiang.homecontrol.api

import com.andrewgiang.homecontrol.addAuthHeader
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenNetworkInterceptor @Inject constructor(val authManager: AuthManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        if (authManager.isAuthenticated()) {

            return chain.proceed(
                originalRequest
                    .newBuilder()
                    .addAuthHeader(authManager.authToken!!.access_token)
                    .build()
            )
        }
        return chain.proceed(originalRequest)
    }

}

