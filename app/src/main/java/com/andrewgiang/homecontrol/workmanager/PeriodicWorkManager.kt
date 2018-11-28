package com.andrewgiang.homecontrol.workmanager

import android.app.Application
import androidx.work.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PeriodicWorkManager @Inject constructor(
    private val workManager: WorkManager,
    app: Application
) {
    private val authWorkerId = app.applicationContext.packageName.plus("auth_refresh_id")
    private val entityId = app.applicationContext.packageName.plus("entity_state_refresh_id")

    private val workRequests = listOf(
        authWorkerId to createWorkRequest<AuthTokenWorker>(repeatInterval = 30, timeUnit = TimeUnit.MINUTES),
        entityId to createWorkRequest<EntitySyncWorker>(repeatInterval = 6, timeUnit = TimeUnit.HOURS)
    )

    fun enqueueWork() {

        workRequests.forEach {
            workManager
                .enqueueUniquePeriodicWork(
                    it.first,
                    ExistingPeriodicWorkPolicy.KEEP,
                    it.second
                )
        }
    }
    private inline fun <reified W : ListenableWorker> createWorkRequest(
        repeatInterval: Long,
        timeUnit: TimeUnit
    ): PeriodicWorkRequest {
        val constraints = defaultConstraints()
        return PeriodicWorkRequestBuilder<W>(repeatInterval, timeUnit)
            .setConstraints(constraints.build())
            .build()
    }

    private fun defaultConstraints(): Constraints.Builder {
        return Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
    }


}