package com.andrewgiang.homecontrol.ui.screens.home

import com.andrewgiang.homecontrol.R
import com.andrewgiang.homecontrol.data.database.model.Action
import com.andrewgiang.homecontrol.data.database.model.Data
import com.andrewgiang.homecontrol.data.model.Icon
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder

data class HomeState(
    val actionIds: List<Action> = hardCodedActions,
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean
)

val hardCodedActions = listOf(
    Action(
        data = Data.ServiceData(
            "group.all_lights",
            "light",
            "turn_off"
        ),
        icon = Icon(
            MaterialDrawableBuilder.IconValue.LIGHTBULB,
            R.color.darkblue_600, R.color.darkblue_100
        ), name = "Off"
    ),
    Action(
        data = Data.ServiceData(
            "group.all_lights",
            "light",
            "turn_on"
        ),
        icon = Icon(
            MaterialDrawableBuilder.IconValue.LIGHTBULB,
            R.color.yellow_600, R.color.yellow_100
        ), name = "On"
    )
)


