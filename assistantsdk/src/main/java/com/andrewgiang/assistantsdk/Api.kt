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
import retrofit2.Retrofit

class Api(retrofit: Retrofit, private val clientId: String) {

    val service: ApiService = retrofit.create(ApiService::class.java)

    fun initialAuth(code: String): Deferred<AuthToken> {
        return service.initialAuth(
            "authorization_code",
            code,
            clientId
        )
    }

    fun service(entityId: List<String>, domain: String, service: String): Deferred<List<Entity>> {
        return this.service.invokeService(
            domain, service,
            EntityBody(entityId)
        )
    }

    fun reauth(token: AuthToken): Deferred<AuthToken> {
        return service.refreshAuth(
            "refresh_token",
            token.refresh_token,
            clientId
        )
    }

    fun getStates(): Deferred<List<Entity>> {
        return service.getStates()
    }

    fun getServices(): Deferred<List<Service>> {
        return service.getServices()
    }
}