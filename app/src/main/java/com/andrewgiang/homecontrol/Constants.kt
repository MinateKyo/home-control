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

package com.andrewgiang.homecontrol

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder

const val CLIENT_ID = "https://andrewgiang.com"
const val REDIRECT_URL = "$CLIENT_ID/authorize"

val APP_ACTION_ICONS = arrayOf(
    MaterialDrawableBuilder.IconValue.HOME,
    MaterialDrawableBuilder.IconValue.HOME_ALERT,
    MaterialDrawableBuilder.IconValue.HOME_ASSISTANT,
    MaterialDrawableBuilder.IconValue.HOME_AUTOMATION,
    MaterialDrawableBuilder.IconValue.BED_EMPTY,
    MaterialDrawableBuilder.IconValue.HOTEL,
    MaterialDrawableBuilder.IconValue.CITY,
    MaterialDrawableBuilder.IconValue.HOSPITAL_BUILDING,
    MaterialDrawableBuilder.IconValue.LIGHTBULB,
    MaterialDrawableBuilder.IconValue.LIGHTBULB_ON,
    MaterialDrawableBuilder.IconValue.LIGHTBULB_ON_OUTLINE,
    MaterialDrawableBuilder.IconValue.LIGHTBULB_OUTLINE,
    MaterialDrawableBuilder.IconValue.FLASH,
    MaterialDrawableBuilder.IconValue.FLASH_OUTLINE,
    MaterialDrawableBuilder.IconValue.FLASHLIGHT,
    MaterialDrawableBuilder.IconValue.FLASH_OFF,
    MaterialDrawableBuilder.IconValue.TOGGLE_SWITCH,
    MaterialDrawableBuilder.IconValue.TOGGLE_SWITCH_OFF,
    MaterialDrawableBuilder.IconValue.BELL,
    MaterialDrawableBuilder.IconValue.BELL_OFF,
    MaterialDrawableBuilder.IconValue.THERMOMETER,
    MaterialDrawableBuilder.IconValue.THERMOMETER_LINES,
    MaterialDrawableBuilder.IconValue.THERMOSTAT,
    MaterialDrawableBuilder.IconValue.THERMOSTAT_BOX,
    MaterialDrawableBuilder.IconValue.CAMERA,
    MaterialDrawableBuilder.IconValue.CAMERA_OFF,
    MaterialDrawableBuilder.IconValue.VIDEO,
    MaterialDrawableBuilder.IconValue.VIDEO_OFF,
    MaterialDrawableBuilder.IconValue.POWER,
    MaterialDrawableBuilder.IconValue.POWER_OFF,
    MaterialDrawableBuilder.IconValue.POWER_ON,
    MaterialDrawableBuilder.IconValue.POWER_CYCLE,
    MaterialDrawableBuilder.IconValue.REMOTE,
    MaterialDrawableBuilder.IconValue.ALARM,
    MaterialDrawableBuilder.IconValue.CLOCK,
    MaterialDrawableBuilder.IconValue.DELETE
)