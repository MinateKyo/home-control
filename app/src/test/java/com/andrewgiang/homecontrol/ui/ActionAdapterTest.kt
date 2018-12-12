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

import android.widget.LinearLayout
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andrewgiang.homecontrol.TestApplication
import com.andrewgiang.homecontrol.data.database.model.Action
import com.andrewgiang.homecontrol.data.model.HomeIcon
import io.mockk.mockk
import io.mockk.verify
import kotlinx.android.synthetic.main.home_actions_layout.view.*
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.util.ReflectionHelpers

@Config(application = TestApplication::class)
@RunWith(AndroidJUnit4::class)
class ActionAdapterTest {
    private val mockClickListener = mockk<ActionClickListener>()

    @Test
    fun `createViewHolder returns non null holder and passes clickListener to viewHolder`() {
        val (_, viewHolder) = createViewHolder()

        assertNotNull(viewHolder)

        assertEquals(mockClickListener, viewHolder.onActionClickListener)
    }

    @Test
    fun `empty adapter will have size 0`() {
        val subject = ActionAdapter(emptyList(), mockClickListener)
        assertTrue(subject.itemCount == 0)
    }

    @Test
    fun `test bind ViewHolder with action of specific position`() {
        val stubList = listOf(Action(0, mockk(), mockk(), "Name", true))
        val subject = ActionAdapter(stubList, mockClickListener)

        val view = mockk<ViewHolder>()
        subject.onBindViewHolder(view, 0)
        verify {
            view.bind(eq(stubList[0]))
        }
    }

    @Test
    fun `bind ViewHolder sets view data from action`() {
        val (action, viewHolder) = createViewHolder()

        viewHolder.bind(action)

        val iconValue = ReflectionHelpers.getField<MaterialDrawableBuilder.IconValue>(viewHolder.itemView.icon, "mIcon")
        assertEquals(action.icon.iconValue, iconValue)
        assertEquals(action.name, viewHolder.itemView.name.text.toString())
    }

    private fun createViewHolder(): Pair<Action, ViewHolder> {
        val parent = LinearLayout(ApplicationProvider.getApplicationContext())
        val subject = ActionAdapter(emptyList(), mockClickListener)
        val action = Action(0, mockk(), HomeIcon(MaterialDrawableBuilder.IconValue.SWITCH_ICON), "Name", true)
        val viewHolder = subject.createViewHolder(parent, 0)
        return Pair(action, viewHolder)
    }
}