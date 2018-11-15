package com.andrewgiang.assistantsdk

import retrofit2.Retrofit

class ApiFactory {

    fun create(
        retrofitBuilder: Retrofit.Builder,
        baseUrl: String,
        clientId: String
    ): Api {
        val retrofit = retrofitBuilder
            .baseUrl(baseUrl)
            .build()
        return Api(retrofit, clientId)
    }
}