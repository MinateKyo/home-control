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