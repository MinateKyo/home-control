package com.andrewgiang.homecontrol.dagger.application

import com.andrewgiang.homecontrol.App
import com.andrewgiang.homecontrol.dagger.component.ControllerComponent
import com.andrewgiang.homecontrol.dagger.component.ControllerModule
import dagger.Component

@ApplicationScope
@Component(modules = [ApplicationModule::class, SecurityModule::class])
interface ApplicationComponent {

    fun inject(app: App)

    fun create(controllerModule: ControllerModule): ControllerComponent

}