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
import com.andrewgiang.homecontrol.ActionShortcutManager
import com.andrewgiang.homecontrol.api.AuthManager
import com.andrewgiang.homecontrol.data.database.model.Action
import com.andrewgiang.homecontrol.data.database.model.Data
import com.andrewgiang.homecontrol.data.model.AppAction
import com.andrewgiang.homecontrol.data.model.Icon
import com.andrewgiang.homecontrol.data.repo.ActionRepo
import com.andrewgiang.homecontrol.testDispatchProvider
import com.andrewgiang.homecontrol.testObserver
import com.andrewgiang.homecontrol.ui.Nav
import com.andrewgiang.homecontrol.ui.controller.HomeControllerDirections
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class HomeViewModelTest {
    val mockActionShortcutManager: ActionShortcutManager = mockk()
    val mockAuth: AuthManager = mockk(relaxed = true)
    val mockActionRepo: ActionRepo = mockk()

    lateinit var subject: HomeViewModel

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        subject = HomeViewModel(
            mockActionShortcutManager,
            mockActionRepo,
            mockAuth,
            testDispatchProvider()
        )
    }

    @Test
    fun `onClick addAction will post navigate to add action controller`() {
        val action = AppAction.AddAction()
        subject.onClick(action)

        assertEquals(
            Nav.Direction(HomeControllerDirections.toAddActionController()),
            subject.getNavState().value
        )
    }

    @Test
    fun `onClick with service data will invoke api request and update in progress state`() {

        val mutableLiveData = MutableLiveData<List<Action>>()
        every { mockActionRepo.actionData() } returns mutableLiveData
        val expectedData = Data.ServiceData("entity", "domain", "service")
        val action = Action(
            data = expectedData,
            icon = Icon(MaterialDrawableBuilder.IconValue.IMPORT_ICON),
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
        assertTrue(observer.observedValues[0]!!.isLoading) // loading from api
        assertFalse(observer.observedValues[1]!!.isLoading) // finish loading
        coVerify {
            mockActionRepo.invokeService(
                eq(expectedData.entityId),
                eq(expectedData.domain),
                eq(expectedData.service)
            )
        }
    }

    @Test
    fun `onShortcutClick with null data will do nothing`() {
        subject.onShortcutClick(null)
        verify { mockActionShortcutManager wasNot Called }
        verify { mockActionRepo wasNot Called }
    }

    @Test
    fun `onShortcutClick with invalid data will do nothing`() {
        every { mockActionShortcutManager.parseShortcutData(eq("valid_data")) } returns null

        subject.onShortcutClick("valid_data")

        verify { mockActionShortcutManager.parseShortcutData(eq("valid_data")) }
        verify { mockActionRepo wasNot Called }
    }

    @Test
    fun `onShortcutClick with valid data will invoke service data`() {

        val expectedData = Data.ServiceData("entity", "domain", "service")
        every { mockActionShortcutManager.parseShortcutData(eq("valid_data")) } returns expectedData

        subject.onShortcutClick("valid_data")

        verify { mockActionShortcutManager.parseShortcutData(eq("valid_data")) }
        coVerify {
            mockActionRepo.invokeService(
                eq(expectedData.entityId),
                eq(expectedData.domain),
                eq(expectedData.service)
            )
        }
    }
}