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

import com.andrewgiang.assistantsdk.response.AuthToken
import com.andrewgiang.homecontrol.data.AuthPrefs
import javax.inject.Inject

class AuthManager @Inject constructor(val authPrefs: AuthPrefs) {

    var authToken: AuthToken? = authPrefs.getAuthToken()

    fun isAuthenticated(): Boolean {
        return authToken != null && authPrefs.getHostUrl() != null
    }

    fun updateAuthToken(newToken: AuthToken) {
        val currentToken = authToken
        authToken = if (isAuthenticated() && currentToken != null) {
            createUpdatedToken(newToken, currentToken)
        } else {
            newToken
        }
        authPrefs.setAuthToken(authToken!!)
    }

    private fun createUpdatedToken(
        newToken: AuthToken,
        currentToken: AuthToken
    ): AuthToken {
        return AuthToken(
            newToken.access_token,
            newToken.expires_in,
            currentToken.refresh_token,
            newToken.token_type
        )
    }

    fun setHost(hostUrl: String) {
        authPrefs.setHostUrl(hostUrl)
    }
}