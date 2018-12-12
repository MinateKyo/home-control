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

package com.andrewgiang.homecontrol.ui

import com.andrewgiang.homecontrol.data.database.model.Action
import com.andrewgiang.homecontrol.data.model.HomeIcon
import io.mockk.mockk
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ActionDiffUtilTest {

    @Test
    fun `areItemsTheSame false if ids do not match`() {
        val action1 = Action(12, mockk(), HomeIcon(MaterialDrawableBuilder.IconValue.SWITCH_ICON), "Item 12", true)
        val action2 = Action(14, mockk(), HomeIcon(MaterialDrawableBuilder.IconValue.SWITCH_ICON), "Item 14", true)
        val subject = ActionDiffUtil(listOf(action1), listOf(action2))
        assertFalse(subject.areItemsTheSame(0, 0))
    }

    @Test
    fun `areItemsTheSame true if ids match`() {
        val action1 = Action(12, mockk(), HomeIcon(MaterialDrawableBuilder.IconValue.SWITCH_ICON), "Item 12", true)
        val action2 = Action(12, mockk(), HomeIcon(MaterialDrawableBuilder.IconValue.SWITCH_ICON), "Item 14", true)
        val subject = ActionDiffUtil(listOf(action1), listOf(action2))
        assertTrue(subject.areItemsTheSame(0, 0))
    }

    @Test
    fun `getOldSize returns from first constructor param`() {
        val oldList = listOf<Action>(mockk(), mockk())
        val subject = ActionDiffUtil(oldList, emptyList())

        assertTrue(subject.oldListSize == 2)
    }

    @Test
    fun `getNewSize returns from second constructor param`() {

        val newList = listOf<Action>(mockk(), mockk())
        val subject = ActionDiffUtil(newList, newList)

        assertTrue(subject.newListSize == 2)
    }

    @Test
    fun `areContentsTheSame returns true only if actions are the equal`() {
        val action1 = Action(12, mockk(), HomeIcon(MaterialDrawableBuilder.IconValue.SWITCH_ICON), "Item 12", true)
        val list = listOf(action1)
        val subject = ActionDiffUtil(list, list)

        assertTrue(subject.areContentsTheSame(0, 0))
    }

    @Test
    fun `areContentsTheSame returns false if actions are not equal`() {
        val action1 = Action(12, mockk(), HomeIcon(MaterialDrawableBuilder.IconValue.SWITCH_ICON), "Item 12", true)
        val newList = listOf(action1)
        val oldList = listOf(action1.copy(id = 45))
        val subject = ActionDiffUtil(oldList, newList)

        assertFalse(subject.areContentsTheSame(0, 0))
    }
}