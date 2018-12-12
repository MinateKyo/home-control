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

package com.andrewgiang.homecontrol.util

import org.junit.Assert.assertEquals
import org.junit.Test
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class AppErrorTest {
    @Test
    fun `create AppError from SocketTimeoutException`() {
        val error = AppError.from(SocketTimeoutException("message here"))
        assertEquals("Network Error: message here", error.msg)
    }

    @Test
    fun `create AppError from SocketTimeoutException without message`() {
        val error = AppError.from(SocketTimeoutException())
        assertEquals("Network Error: Unknown", error.msg)
    }

    @Test
    fun `create AppError from UnknownHostException`() {
        val error = AppError.from(UnknownHostException("message here"))
        assertEquals("Network Error: message here", error.msg)
    }

    @Test
    fun `create AppError from UnknownHostException without message`() {
        val error = AppError.from(UnknownHostException())
        assertEquals("Network Error: Unknown", error.msg)
    }

    @Test
    fun `create AppError from unknown Exception type`() {
        val error = AppError.from(NullPointerException())
        assertEquals(AppError.UnknownError, error)
    }
}