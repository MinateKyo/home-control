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
import com.andrewgiang.homecontrol.data.database.model.Action
import com.andrewgiang.homecontrol.data.model.HomeIcon
import com.andrewgiang.homecontrol.data.repo.ActionRepo
import com.andrewgiang.homecontrol.testDispatchProvider
import com.andrewgiang.homecontrol.ui.Nav
import com.andrewgiang.homecontrol.ui.controller.IconEditControllerDirections
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class IconEditViewModelTest {

    private lateinit var subject: IconEditViewModel
    private val mockActionRepo: ActionRepo = mockk()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        subject = IconEditViewModel(
            mockActionRepo,
            testDispatchProvider()
        )
    }

    @Test
    fun `load editable action that is in db will post ui model from action`() {
        val mockAction = mockk<Action>()
        val expectedIcon = HomeIcon(MaterialDrawableBuilder.IconValue.ALARM_LIGHT)
        every { mockAction.icon } returns expectedIcon
        val expectedName = "actionName"
        every { mockAction.name } returns expectedName

        coEvery { mockActionRepo.getAction(eq(1)) } returns mockAction

        subject.loadEditableAction(1)

        assertEquals(
            IconUiModel(expectedIcon, expectedName, mockAction),
            subject.getUiModel().value!!
        )
    }

    @Test
    fun `add action will insert action into repo and update view state to shouldFinish true `() {
        coEvery { mockActionRepo.insertAction(any()) } just Runs

        subject.onAddButtonClicked(
            listOf(),
            "domain.service",
            "displayName",
            true
        )

        assertEquals(
            Nav.Direction(IconEditControllerDirections.toHome()),
            subject.getNavState().value!!
        )
    }

    @Test
    fun `on icon color selection will update ui model`() {
        val original = subject.getUiModel().value!!.homeIcon.iconColorInt

        val newColor = 999
        subject.onIconColorSelected(newColor)

        assertNotEquals(original, newColor)

        assertEquals(newColor, subject.getUiModel().value!!.homeIcon.iconColorInt)
    }

    @Test
    fun `on display changed updated ui model`() {
        val original = subject.getUiModel().value!!.displayName

        val displayName = "new display name"
        subject.onTextChanged(displayName)

        assertNotEquals(original, displayName)

        assertEquals(displayName, subject.getUiModel().value!!.displayName)
    }

    @Test
    fun `on background color changed updated ui model`() {
        val original = subject.getUiModel().value!!.homeIcon.backgroundColorInt

        val newColor = 999
        subject.onBackgroundColorSelected(newColor)

        assertNotEquals(original, newColor)

        assertEquals(newColor, subject.getUiModel().value!!.homeIcon.backgroundColorInt)
    }

    @Test
    fun `on icon select change updated ui model`() {
        val original = subject.getUiModel().value!!.homeIcon.iconValue

        val iconValue = MaterialDrawableBuilder.IconValue.PACKAGE_ICON
        subject.onIconSelected(iconValue)

        assertNotEquals(original, iconValue)

        assertEquals(iconValue, subject.getUiModel().value!!.homeIcon.iconValue)
    }
}