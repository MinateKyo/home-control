package com.andrewgiang.homecontrol

import android.app.Application
import com.andrewgiang.homecontrol.dependencyinjection.application.ApplicationComponent
import com.andrewgiang.homecontrol.dependencyinjection.application.ApplicationModule
import com.andrewgiang.homecontrol.dependencyinjection.application.DaggerApplicationComponent
import com.facebook.soloader.SoLoader
import timber.log.Timber

class App : Application() {

    val applicationComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder()
            .applicationModule(
                ApplicationModule(this@App)
            ).build()
    }


    override fun onCreate() {
        super.onCreate()
        SoLoader.init(this, false)
        applicationComponent.inject(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}