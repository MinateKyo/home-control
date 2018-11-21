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
        authWorkerId to authWorkRequest(),
        entityId to entityRefreshRequest()
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

    private fun entityRefreshRequest(): PeriodicWorkRequest {
        val constraints = defaultConstraints()
        return PeriodicWorkRequestBuilder<EntitySyncWorker>(6, TimeUnit.HOURS)
            .setConstraints(constraints.build())
            .build()
    }

    private fun authWorkRequest(): PeriodicWorkRequest {
        val constraints = defaultConstraints()
        return PeriodicWorkRequestBuilder<AuthTokenWorker>(25, TimeUnit.MINUTES)
            .setConstraints(constraints.build())
            .build()
    }

    private fun defaultConstraints(): Constraints.Builder {
        return Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
    }


}