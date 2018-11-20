package com.andrewgiang.assistantsdk

import com.andrewgiang.assistantsdk.request.EntityBody
import com.andrewgiang.assistantsdk.response.AuthToken
import com.andrewgiang.assistantsdk.response.Entity
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit

class Api(retrofit: Retrofit, val clientId: String) {

    val service = retrofit.create(ApiService::class.java)

    fun initialAuth(code: String): Deferred<AuthToken> {
        return service.initialAuth(
            "authorization_code",
            code,
            clientId
        )
    }

    fun service(entityId: String, domain: String, service: String): Deferred<List<Entity>> {
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


}