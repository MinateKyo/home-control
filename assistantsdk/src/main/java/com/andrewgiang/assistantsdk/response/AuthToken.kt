package com.andrewgiang.assistantsdk.response

data class AuthToken(
    val access_token: String,
    val expires_in: Int,
    val refresh_token: String,
    val token_type: String
)