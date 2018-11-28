package com.andrewgiang.homecontrol.dagger.application

import com.andrewgiang.homecontrol.App
import com.andrewgiang.homecontrol.dagger.component.ApiModule
import com.andrewgiang.homecontrol.dagger.component.ControllerComponent
import com.andrewgiang.homecontrol.dagger.component.ControllerModule
import com.andrewgiang.homecontrol.workmanager.AuthTokenWorker
import com.andrewgiang.homecontrol.workmanager.EntitySyncWorker
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        ApplicationModule::class,
        SecurityModule::class,
        ApiModule::class,
        DatabaseModule::class
    ]
)
interface ApplicationComponent {

    fun inject(app: App)
    fun inject(tokenWorker: AuthTokenWorker)
    fun inject(entitySyncWorker: EntitySyncWorker)
    fun create(controllerModule: ControllerModule): ControllerComponent
}