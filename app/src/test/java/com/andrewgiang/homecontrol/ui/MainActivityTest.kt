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

import androidx.core.view.isVisible
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andrewgiang.homecontrol.TestApplication
import com.andrewgiang.homecontrol.api.AuthManager
import com.andrewgiang.homecontrol.currentNavFragment
import com.andrewgiang.homecontrol.getTestApp
import com.andrewgiang.homecontrol.ui.controller.AddActionController
import com.andrewgiang.homecontrol.ui.controller.HomeController
import com.andrewgiang.homecontrol.ui.controller.IconEditController
import com.andrewgiang.homecontrol.ui.controller.SetupController
import kotlinx.android.synthetic.main.fragment_add_action.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.robolectric.annotation.Config

@Config(application = TestApplication::class)
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    lateinit var mockAuthManager: AuthManager

    @Before
    fun setUp() {
        mockAuthManager = getTestApp().testAppModule.mockAuthManager
    }

    @Test
    fun test_unauth_user_is_on_setup_controller() {
        `when`(mockAuthManager.isAuthenticated()).thenReturn(false)

        ActivityScenario.launch(MainActivity::class.java)
            .onActivity { activity ->
                assertTrue(activity.currentNavFragment is SetupController)

                val container =
                    (activity.currentNavFragment as SetupController).container

                assertTrue(container.nextButton.isEnabled)
            }
    }

    @Test
    fun test_auth_user_is_on_home_controller() {
        `when`(mockAuthManager.isAuthenticated()).thenReturn(true)

        ActivityScenario.launch(MainActivity::class.java)
            .onActivity { activity ->
                assertTrue(activity.currentNavFragment is HomeController)

                val container =
                    (activity.currentNavFragment as HomeController).container
                assertTrue(container.fab.isVisible)
            }
    }

    @Test
    fun test_when_on_home_controller_and_click_add_button_navigates_to_AddActionController() {
        `when`(mockAuthManager.isAuthenticated()).thenReturn(true)

        ActivityScenario.launch(MainActivity::class.java)
            .onActivity { activity ->
                assertTrue(activity.currentNavFragment is HomeController)

                navigateToAddActionController(activity)

                assertTrue(activity.currentNavFragment is AddActionController)
            }
    }

    @Test
    fun test_when_on_AddActionController_and_click_next_button_navigates_to_IconEditController() {
        `when`(mockAuthManager.isAuthenticated()).thenReturn(true)

        ActivityScenario.launch(MainActivity::class.java)
            .onActivity { activity ->
                assertTrue(activity.currentNavFragment is HomeController)

                navigateToAddActionController(activity)

                val addActionController = activity.currentNavFragment as AddActionController

                addActionController.container.showServices(listOf("item1", "item2"))
                addActionController.container.nextButton.performClick()

                assertTrue(activity.currentNavFragment is IconEditController)
            }
    }

    private fun navigateToAddActionController(activity: MainActivity) {
        val container =
            (activity.currentNavFragment as HomeController).container
        container.containerView.fab.performClick()
    }
}