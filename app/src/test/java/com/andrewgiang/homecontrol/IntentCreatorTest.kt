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

package com.andrewgiang.homecontrol

import android.content.Context
import com.andrewgiang.homecontrol.util.IntentCreator
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