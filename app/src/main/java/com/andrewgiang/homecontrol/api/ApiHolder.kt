package com.andrewgiang.homecontrol.api

import com.andrewgiang.assistantsdk.Api
import com.andrewgiang.assistantsdk.ApiFactory
import com.andrewgiang.homecontrol.CLIENT_ID
import com.andrewgiang.homecontrol.data.AuthPrefs
import retrofit2.Retrofit
import javax.inject.Inject

class ApiHolder @Inject constructor(
    builder: Retrofit.Builder,
    authPrefs: AuthPrefs,
    apiFactory: ApiFactory
) {

    val api: Api by lazy {
        val hostUrl = authPrefs.getHostUrl()
        if (hostUrl != null) {
            val newApi = apiFactory.create(builder, hostUrl, CLIENT_ID)
            AuthTokenAuthenticator.api = newApi
            return@lazy newApi
        } else {
            throw IllegalStateException("Accessing API before url is set")
        }
    }


}