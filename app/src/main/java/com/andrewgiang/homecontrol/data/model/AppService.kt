package com.andrewgiang.homecontrol.data.model

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder


sealed class AppAction constructor(
    appData: Data.AppData = Data.AppData(),
    icon: Icon,
    name: String
) : Action(appData, icon, name) {

    class FullScreen : AppAction(
        icon = Icon(MaterialDrawableBuilder.IconValue.FULLSCREEN),
        name = "Full Screen"
    )

    class AddAction : AppAction(
        icon = Icon(MaterialDrawableBuilder.IconValue.PLUS),
        name = "Add"
    )
}