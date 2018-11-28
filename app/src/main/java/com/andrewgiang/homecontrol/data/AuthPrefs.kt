package com.andrewgiang.homecontrol.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.andrewgiang.assistantsdk.response.AuthToken
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import javax.inject.Inject

interface AuthPrefs {
    fun getHostUrl(): String?
    fun setHostUrl(hostUrl: String)
    fun setAuthToken(authToken: AuthToken)
    fun getAuthToken(): AuthToken?
}

class AuthPrefsSecure @Inject constructor(
    val sharedPreferences: SharedPreferences,
    moshi: Moshi,
    val encryption: Encryption
) : AuthPrefs {
    private val authTokenAdapter: JsonAdapter<AuthToken> = moshi.adapter(AuthToken::class.java)

    companion object {
        const val KEY_HOST_URL = "host_url"
        const val KEY_TOKEN = "token"
    }

    // TODO refactor to use suspend function
    override fun setAuthToken(authToken: AuthToken) {
        sharedPreferences.edit {
            putString(KEY_TOKEN, encryption.encrypt(authTokenAdapter.toJson(authToken)))
            commit()
        }
    }

    override fun getAuthToken(): AuthToken? {
        val json = sharedPreferences.getString(KEY_TOKEN, null)
        if (json != null) {
            return authTokenAdapter.fromJson(encryption.decrypt(json))
        }
        return null
    }

    override fun getHostUrl(): String? {
        return sharedPreferences.getString(KEY_HOST_URL, "")
    }

    override fun setHostUrl(hostUrl: String) {
        sharedPreferences.edit {
            putString(KEY_HOST_URL, hostUrl)
            commit()
        }
    }
}