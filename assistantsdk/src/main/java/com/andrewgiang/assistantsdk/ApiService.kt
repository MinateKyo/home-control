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

package com.andrewgiang.assistantsdk

import com.andrewgiang.assistantsdk.request.EntityBody
import com.andrewgiang.assistantsdk.response.AuthToken
import com.andrewgiang.assistantsdk.response.Entity
import com.andrewgiang.assistantsdk.response.Service
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("/auth/token")
    fun initialAuth(
        @Field("grant_type") grantType: String,
        @Field("code") code: String,
        @Field("client_id") clientId: String
    ): Deferred<AuthToken>

    @FormUrlEncoded
    @POST("/auth/token")
    fun refreshAuth(
        @Field("grant_type") grantType: String,
        @Field("refresh_token") code: String,
        @Field("client_id") clientId: String
    ): Deferred<AuthToken>


    @POST("/api/services/{domain}/{service}")
    fun invokeService(
        @Path("domain") domain: String,
        @Path("service") service: String,
        @Body entityBody: EntityBody
    ): Deferred<List<Entity>>


    @GET("/api/states")
    fun getStates(): Deferred<List<Entity>>

    @GET("/api/services")
    fun getServices(): Deferred<List<Service>>
}

