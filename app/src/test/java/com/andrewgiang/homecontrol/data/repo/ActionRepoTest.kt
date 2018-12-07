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

package com.andrewgiang.homecontrol.data.repo

import com.andrewgiang.assistantsdk.Api
import com.andrewgiang.assistantsdk.response.Entity
import com.andrewgiang.assistantsdk.response.Service
import com.andrewgiang.assistantsdk.response.ServiceInfo
import com.andrewgiang.homecontrol.api.ApiHolder
import com.andrewgiang.homecontrol.data.database.dao.ActionDao
import com.andrewgiang.homecontrol.data.database.model.Action
import com.andrewgiang.homecontrol.testDispatchProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ActionRepoTest {
    private val mockActionDao: ActionDao = mockk(relaxed = true)
    private val mockApiHolder: ApiHolder = mockk()
    private val mockApi: Api = mockk()

    val subject = ActionRepo(mockActionDao, mockApiHolder, testDispatchProvider())

    @Before
    fun setUp() {
        every { mockApiHolder.api } returns mockApi
    }

    @Test
    fun `response service will flat map to domain{dot}service list`() = runBlocking {
        val listOf = listOf(
            Service(
                "light", services = mapOf(
                    "turn_off" to ServiceInfo("desc", mapOf()),
                    "toggle" to ServiceInfo("desc", mapOf())
                )
            )
        )
        coEvery { mockApi.getServices().await() } returns listOf

        assertEquals(listOf("light.turn_off", "light.toggle"), subject.getDomainServiceList())
    }

    @Test
    fun `insert action will delegate to actionDao`() = runBlocking {
        val action = mockk<Action>()
        subject.insertAction(action)

        coVerify { mockActionDao.insertAction(eq(action)) }
    }

    @Test
    fun `actionData will delegate to actionDao`() = runBlocking {
        subject.actionData()
        coVerify { mockActionDao.getActions() }
    }

    @Test
    fun `invokeService will call api service and return response`() = runBlocking {
        val entitiyIds = listOf("entity.id", "entitiy2.id")
        val entityResponse = listOf(Entity("entity.id", "on", mapOf()))

        coEvery { mockApi.service(eq(entitiyIds), eq("domain"), eq("service")).await() } returns entityResponse

        assertEquals(entityResponse, subject.invokeService(entitiyIds, "domain", "service"))
    }
}