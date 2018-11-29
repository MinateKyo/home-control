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

package com.andrewgiang.homecontrol.dagger.component

import android.app.Application
import com.andrewgiang.assistantsdk.ApiFactory
import com.andrewgiang.homecontrol.api.AuthTokenAuthenticator
import com.andrewgiang.homecontrol.api.TokenNetworkInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
class ApiModule {

    @Provides
    fun retrofit(okHttpClient: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create())
    }

    @Provides
    fun okClient(
        authenticator: AuthTokenAuthenticator,
        tokenNetworkInterceptor: TokenNetworkInterceptor,
        application: Application
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .authenticator(authenticator)
            .addNetworkInterceptor(ChuckInterceptor(application))
            .addNetworkInterceptor(tokenNetworkInterceptor)
            .build()
    }

    @Provides
    fun apiFactory(): ApiFactory {
        return ApiFactory()
    }
}