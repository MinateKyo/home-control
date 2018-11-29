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
