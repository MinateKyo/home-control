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

package com.andrewgiang.homecontrol.data.database

import com.andrewgiang.homecontrol.data.database.model.Data
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ConvertersTest {

    @Before
    fun setUp() {
    }

    @Test
    fun dataToAndFromJson() {
        val converters = Converters()
        val data = Data.ServiceData("entity", "domain", "service")
        val dataJson = converters.dataToString(data)
        assertEquals(data, converters.dataFromMetaJsonString(dataJson))
    }
}