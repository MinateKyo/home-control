package com.andrewgiang.homecontrol

import android.app.Application
import com.andrewgiang.homecontrol.dagger.application.ApplicationComponent
import com.andrewgiang.homecontrol.dagger.application.ApplicationModule
import com.andrewgiang.homecontrol.dagger.application.DaggerApplicationComponent
import com.andrewgiang.homecontrol.workmanager.PeriodicWorkManager
import com.facebook.soloader.SoLoader
import timber.log.Timber
import javax.inject.Inject

class App : Application() {
    @Inject
    lateinit var periodicWorkManager: PeriodicWorkManager

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
        periodicWorkManager.enqueueWork()
    }
}