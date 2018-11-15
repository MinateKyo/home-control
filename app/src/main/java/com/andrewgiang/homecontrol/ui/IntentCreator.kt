package com.andrewgiang.homecontrol.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.andrewgiang.homecontrol.CLIENT_ID
import com.andrewgiang.homecontrol.REDIRECT_URL
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

private fun HttpUrl.androidUri(): Uri {
    return Uri.parse(this.toString())
}
