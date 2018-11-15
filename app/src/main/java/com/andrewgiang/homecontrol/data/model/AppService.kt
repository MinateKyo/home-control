package com.andrewgiang.homecontrol.data.model

import com.andrewgiang.assistantsdk.request.Service


sealed class AppAction constructor(domain: String = "app", action: String) : Service(domain, action) {

    class FullScreen : AppAction(action = "fullscreen")
    class AddAction : AppAction(action = "add")
    class Climate : AppAction("climate", "set_temperature")

}


