package com.andrewgiang.homecontrol

import okhttp3.Request

fun Request.Builder.addAuthHeader(token: String): Request.Builder {
    val name = "Authorization"
    val bearerToken = "Bearer $token"
    this.header(name, bearerToken)
    return this
}
