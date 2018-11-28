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
        return if (!authManager.isAuthenticated()) {
            Result.SUCCESS
        } else {
            refreshState()
        }
    }

    private fun refreshState(): Result {
        return runBlocking {
            return@runBlocking try {
                entityRepo.refreshStates()
                Result.SUCCESS
            } catch (e: Exception) {
                Result.RETRY
            }
        }
    }
}