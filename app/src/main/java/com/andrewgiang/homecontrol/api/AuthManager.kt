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