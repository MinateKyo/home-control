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

package com.andrewgiang.homecontrol.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.andrewgiang.homecontrol.api.AuthManager
import com.andrewgiang.homecontrol.data.database.model.Action
import com.andrewgiang.homecontrol.data.database.model.Data
import com.andrewgiang.homecontrol.data.model.HomeIcon
import com.andrewgiang.homecontrol.data.repo.ActionRepo
import com.andrewgiang.homecontrol.testDispatchProvider
import com.andrewgiang.homecontrol.testObserver
import com.andrewgiang.homecontrol.ui.Nav
import com.andrewgiang.homecontrol.ui.controller.HomeControllerDirections
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class HomeViewModelTest {
    val mockAuth: AuthManager = mockk(relaxed = true)
    val mockActionRepo: ActionRepo = mockk()

    lateinit var subject: HomeViewModel

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        subject = HomeViewModel(
            mockActionRepo,
            mockAuth,
            testDispatchProvider()
        )
    }

    @Test
    fun `onClick addAction will post navigate to add action controller`() {
        subject.onAddActionClick()

        assertEquals(
            Nav.Direction(HomeControllerDirections.toActionController()),
            subject.getNavState().value
        )
    }

    @Test
    fun `onClick with service data will invoke api request and update in progress state`() {

        val mutableLiveData = MutableLiveData<List<Action>>()
        every { mockActionRepo.actionData() } returns mutableLiveData
        val expectedData = Data("entity", "domain", "service")
        val action = Action(
            data = expectedData,
            icon = HomeIcon(MaterialDrawableBuilder.IconValue.IMPORT_ICON),
            name = "Name",
            isShortcut = false
        )

        coEvery {
            mockActionRepo.invokeService(
                eq(expectedData.entityId),
                eq(expectedData.domain),
                eq(expectedData.service)
            )
        } returns mockk()
        val observer = subject.getViewState().testObserver()
        subject.onClick(action)
        assertTrue(observer.observedValues[0]!!.loading.isLoading) // loading from api
        assertFalse(observer.observedValues[1]!!.loading.isLoading) // finish loading
        coVerify {
            mockActionRepo.invokeService(
                eq(expectedData.entityId),
                eq(expectedData.domain),
                eq(expectedData.service)
            )
        }
    }

    @Test
    fun `onShortcutClick with repo returns null data will do nothing`() {
        coEvery { mockActionRepo.getAction(eq(13)) } returns null
        subject.onShortcutClick(13L)

        coVerify(inverse = true) { mockActionRepo.invokeService(any(), any(), any()) }
    }

    @Test
    fun `onShortcutClick with null id returns null data will do nothing`() {
        subject.onShortcutClick(null)

        coVerify(inverse = true) { mockActionRepo.invokeService(any(), any(), any()) }
    }

    @Test
    fun `onShortcutClick with valid action will invoke service data`() {
        val expectedData = Data("entity", "domain", "service")
        val actionId: Long = 13
        val stubAction = Action(0, expectedData, mockk(), "name", false)
        coEvery { mockActionRepo.getAction(eq(actionId)) } returns stubAction

        subject.onShortcutClick(actionId)

        coVerify {
            mockActionRepo.invokeService(
                eq(expectedData.entityId),
                eq(expectedData.domain),
                eq(expectedData.service)
            )
        }
    }

    @Test
    fun `on delete action will remove action from repo`() {
        val action = mockk<Action>()
        subject.onDelete(action)

        coVerify {
            mockActionRepo.removeAction(eq(action))
        }
    }
}