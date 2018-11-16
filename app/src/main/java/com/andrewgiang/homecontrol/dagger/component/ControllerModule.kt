package com.andrewgiang.homecontrol.dagger.component

import android.app.Activity
import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import dagger.Module
import dagger.Provides

@Module
class ControllerModule(val activity: FragmentActivity) {

    @Provides
    fun context(): Context {
        return activity
    }

    @Provides
    fun activity(): Activity {
        return activity
    }

    @Provides
    fun fragmentManager(): FragmentManager {
        return activity.supportFragmentManager
    }

}

