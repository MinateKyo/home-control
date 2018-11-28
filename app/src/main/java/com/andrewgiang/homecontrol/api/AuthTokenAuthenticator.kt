package com.andrewgiang.homecontrol.api

import com.andrewgiang.assistantsdk.Api
import com.andrewgiang.homecontrol.addAuthHeader
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.io.IOException
import javax.inject.Inject

class AuthTokenAuthenticator @Inject constructor(val authManager: AuthManager) : Authenticator {

    @Throws(IOException::class)
    override fun authenticate(route: Route, response: Response): Request? {
        val api = api
        val currentToken = authManager.authToken

        if (api != null && currentToken != null) {
            val token = runBlocking { api.reauth(currentToken).await() }
            authManager.updateAuthToken(token)
            return response.request()
                .newBuilder()
                .addAuthHeader(token.access_token)
                .build()
        }
        return null
    }

    companion object {
        var api: Api? = null
    }
}
