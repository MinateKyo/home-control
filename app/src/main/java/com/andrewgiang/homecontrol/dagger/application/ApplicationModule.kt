package com.andrewgiang.homecontrol.dagger.application

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.ShortcutManager
import androidx.work.WorkManager
import com.andrewgiang.homecontrol.DispatchProvider
import com.andrewgiang.homecontrol.api.AuthManager
import com.andrewgiang.homecontrol.data.AuthPrefs
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers


@Module
class ApplicationModule(val app: Application) {

    @ApplicationScope
    @Provides
    fun app(): Application {
        return app
    }

    @ApplicationScope
    @Provides
    fun authManager(authPrefs: AuthPrefs): AuthManager {
        return AuthManager(authPrefs)
    }

    @ApplicationScope
    @Provides
    fun workManager(): WorkManager {
        return WorkManager.getInstance()
    }

    @ApplicationScope
    @Provides
    fun moshi(): Moshi {
        return Moshi.Builder().build()
    }

    @SuppressLint("NewApi")
    @ApplicationScope
    @Provides
    fun shortcutManager(app: Application): ShortcutManager {
        return app.getSystemService(Context.SHORTCUT_SERVICE) as ShortcutManager
    }

    @ApplicationScope
    @Provides
    fun dispatchers(): DispatchProvider {
        return DispatchProvider(Dispatchers.IO, Dispatchers.Main)
    }


}