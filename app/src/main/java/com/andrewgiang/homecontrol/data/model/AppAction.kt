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