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
import com.andrewgiang.assistantsdk.ApiFactory
import com.andrewgiang.homecontrol.CLIENT_ID
import com.andrewgiang.homecontrol.data.AuthPrefs
import retrofit2.Retrofit
import javax.inject.Inject

class ApiHolder @Inject constructor(
    val builder: Retrofit.Builder,
    authPrefs: AuthPrefs,
    val apiFactory: ApiFactory
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

    fun create(hostUrl: String): Api {
        return apiFactory.create(builder, hostUrl, CLIENT_ID)
    }
}