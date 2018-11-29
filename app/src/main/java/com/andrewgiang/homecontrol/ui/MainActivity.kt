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

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.andrewgiang.homecontrol.App
import com.andrewgiang.homecontrol.R
import com.andrewgiang.homecontrol.dagger.component.ControllerComponent
import com.andrewgiang.homecontrol.dagger.component.ControllerModule

class MainActivity : AppCompatActivity() {

    val controllerComponent: ControllerComponent by lazy {
        (application as App).applicationComponent
            .create(ControllerModule(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host).navigateUp()
}
