package com.andrewgiang.assistantsdk

import com.andrewgiang.assistantsdk.request.EntityBody
import com.andrewgiang.assistantsdk.response.AuthToken
import com.andrewgiang.assistantsdk.response.Entity
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

}

