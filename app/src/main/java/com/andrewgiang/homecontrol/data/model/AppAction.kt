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

package com.andrewgiang.homecontrol.data.model

import com.andrewgiang.homecontrol.data.database.model.Action
import com.andrewgiang.homecontrol.data.database.model.Data
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder

sealed class AppAction constructor(
    appData: Data.AppData = Data.AppData(),
    icon: Icon,
    name: String
) : Action(0, appData, icon, name, false) {

    class FullScreen : AppAction(
        icon = Icon(MaterialDrawableBuilder.IconValue.FULLSCREEN),
        name = "Full Screen"
    )

    class AddAction : AppAction(
        icon = Icon(MaterialDrawableBuilder.IconValue.PLUS),
        name = "Add"
    )
}