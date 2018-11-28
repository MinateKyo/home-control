package com.andrewgiang.homecontrol.ui

import android.content.Context
import io.mockk.mockk
import okhttp3.HttpUrl
import org.junit.Assert.assertEquals
import org.junit.Test

class IntentCreatorTest {

    @Test
    fun buildAuthorizeUrl() {
        val context = mockk<Context>()
        val subject = IntentCreator(context)

        val baseUrl = HttpUrl.parse("https://www.your-instance.com")!!

        val url = subject.buildAuthorizeUrl(baseUrl)

        assertEquals(
            HttpUrl.parse("https://www.your-instance.com/auth/authorize?client_id=https%3A%2F%2Fandrewgiang.com&redirect_uri=https%3A%2F%2Fandrewgiang.com%2Fauthorize"),
            url
        )
    }
}