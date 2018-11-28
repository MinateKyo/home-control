package com.andrewgiang.homecontrol

import android.net.Uri
import okhttp3.HttpUrl
import okhttp3.Request

fun Request.Builder.addAuthHeader(token: String): Request.Builder {
    val name = "Authorization"
    val bearerToken = "Bearer $token"
    this.header(name, bearerToken)
    return this
}

fun HttpUrl.androidUri(): Uri {
    return Uri.parse(this.toString())
}
