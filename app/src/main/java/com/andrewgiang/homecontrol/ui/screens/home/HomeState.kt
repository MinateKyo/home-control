package com.andrewgiang.homecontrol.ui.screens.home

import com.andrewgiang.assistantsdk.request.Service
import com.andrewgiang.homecontrol.R
import com.andrewgiang.homecontrol.data.model.Action
import com.andrewgiang.homecontrol.data.model.AppAction
import com.andrewgiang.homecontrol.data.model.Icon
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder

data class HomeState(
    val actionIds: List<Action> = hardCodedActions,
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean
)

val hardCodedActions = listOf(
    Action(
        "group.all_lights",
        Service("light", "turn_off"),
        Icon(
            MaterialDrawableBuilder.IconValue.LIGHTBULB,
            R.color.darkblue_600, R.color.darkblue_100
        ), "Off"
    ),
    Action(
        "group.all_lights",
        Service("light", "turn_on"),
        Icon(
            MaterialDrawableBuilder.IconValue.LIGHTBULB,
            R.color.yellow_600, R.color.yellow_100
        ), "On"
    ),
    Action(
        "app.fullscreen",
        AppAction.FullScreen(),
        Icon(MaterialDrawableBuilder.IconValue.FULLSCREEN),
        "Fullscreen"
    )

)


