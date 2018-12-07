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
import com.andrewgiang.homecontrol.api.ApiHolder
import com.andrewgiang.homecontrol.data.database.dao.EntityDao
import com.andrewgiang.homecontrol.data.database.model.Entity
import com.andrewgiang.homecontrol.testDispatchProvider
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

typealias ReposeEntity = com.andrewgiang.assistantsdk.response.Entity

class EntityRepoTest {
    private val mockEntityDao: EntityDao = mockk(relaxed = true)
    private val mockApiHolder: ApiHolder = mockk()
    private val mockApi: Api = mockk()

    val subject = EntityRepo(mockEntityDao, mockApiHolder, testDispatchProvider())

    @Before
    fun setUp() {
        every { mockApiHolder.api } returns mockApi
    }

    @Test
    fun `get entities with filtered domain`() = runBlocking {

        val listOf = listOf(stubEntity("domain.test"), stubEntity("not_matching.test"))
        coEvery { mockEntityDao.getEntitiesSync() } returns listOf

        val filteredList = subject.getEntity("domain")

        assertEquals(listOf(stubEntity("domain.test")), filteredList)
    }

    @Test
    fun `get entities with filtered domain with groups will be included in result if all domains inside group match`() =
        runBlocking {
            val entities = listOf(
                stubEntity("light.home"),
                // all domains match "light"
                stubGroup(
                    arrayListOf(
                        "light.home",
                        "light.living_room"
                    )
                )
            )

            coEvery { mockEntityDao.getEntitiesSync() } returns entities

            assertEquals(entities, subject.getEntity("light"))
        }

    @Test
    fun `get entities with filtered domain will not return group with mixed domain type`() =
        runBlocking {
            val entities = listOf(
                stubEntity("light.home"),
                // mixed domains will not be included in result
                stubGroup(
                    arrayListOf(
                        "light.home",
                        "other_domain.living_room"
                    )
                )
            )

            coEvery { mockEntityDao.getEntitiesSync() } returns entities

            assertEquals(listOf(stubEntity("light.home")), subject.getEntity("light"))
        }

    @Test
    fun `refreshStates will insert into entity dao`() = runBlocking {

        val response = listOf(
            ReposeEntity(
                "id",
                "",
                mapOf()
            )
        )
        coEvery { mockApi.getStates().await() } returns response
        subject.refreshStates()
        verify { mockEntityDao.insert(listOf(stubEntity("id"))) }
    }

    private fun stubGroup(entities: ArrayList<String>): Entity {
        val groupAttributes = mutableMapOf<String, Any>().apply {
            this["entity_id"] = entities
        }

        return stubEntity("group.all_lights", groupAttributes)
    }

    private fun stubEntity(entityId: String, map: Map<String, Any> = mapOf()): Entity {
        return Entity(entityId, "", map)
    }
}