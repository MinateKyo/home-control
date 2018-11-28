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