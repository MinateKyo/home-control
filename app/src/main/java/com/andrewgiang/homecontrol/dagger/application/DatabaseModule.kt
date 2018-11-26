package com.andrewgiang.homecontrol.dagger.application

import android.app.Application
import androidx.room.Room
import com.andrewgiang.homecontrol.BuildConfig
import com.andrewgiang.homecontrol.data.database.AppDatabase
import com.andrewgiang.homecontrol.data.database.dao.ActionDao
import com.andrewgiang.homecontrol.data.database.dao.EntityDao
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {

    @ApplicationScope
    @Provides
    fun db(app: Application): AppDatabase {
        val builder = Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "app.db"
        )
        if (BuildConfig.DEBUG) {
            builder.fallbackToDestructiveMigration()
        }
        return builder.build()
    }

    @ApplicationScope
    @Provides
    fun entityDao(appDatabase: AppDatabase): EntityDao {
        return appDatabase.entityDao()
    }

    @ApplicationScope
    @Provides
    fun actionDao(appDatabase: AppDatabase): ActionDao {
        return appDatabase.actionDao()
    }


}