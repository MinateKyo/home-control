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

package com.andrewgiang.homecontrol.util

import android.content.Context
import android.content.Intent
import com.andrewgiang.homecontrol.CLIENT_ID
import com.andrewgiang.homecontrol.REDIRECT_URL
import com.andrewgiang.homecontrol.androidUri
import okhttp3.HttpUrl
import javax.inject.Inject

class IntentCreator @Inject constructor(val context: Context) {

    fun sendAuthorizeIntent(baseUrl: HttpUrl) {

        val authorizeUrl = buildAuthorizeUrl(baseUrl)

        val intent = Intent(
            Intent.ACTION_VIEW,
            authorizeUrl.androidUri()
        )
        context.startActivity(intent)
    }

    fun buildAuthorizeUrl(baseUrl: HttpUrl): HttpUrl {
        return baseUrl.newBuilder()
            .addPathSegment("auth")
            .addPathSegment("authorize")
            .addQueryParameter("client_id", CLIENT_ID)
            .addQueryParameter("redirect_uri", REDIRECT_URL)
            .build()
    }
}
