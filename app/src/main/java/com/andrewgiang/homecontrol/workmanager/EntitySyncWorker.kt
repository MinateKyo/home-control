package com.andrewgiang.homecontrol.workmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.andrewgiang.homecontrol.App
import com.andrewgiang.homecontrol.api.AuthManager
import com.andrewgiang.homecontrol.data.repo.EntityRepo
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class EntitySyncWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    @Inject
    lateinit var authManager: AuthManager
    @Inject
    lateinit var entityRepo: EntityRepo

    override fun doWork(): Result {
        (applicationContext as App).applicationComponent.inject(this)
        if (authManager.isAuthenticated()) {
            return runBlocking {
                try {
                    val refreshStates = entityRepo.refreshStates()
                    if (!refreshStates.isNullOrEmpty()) {
                        return@runBlocking Result.SUCCESS
                    }

                } catch (e: Exception) {
                    return@runBlocking Result.RETRY
                }
                return@runBlocking Result.SUCCESS
            }

        }
        return Result.SUCCESS
    }

}