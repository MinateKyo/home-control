package com.andrewgiang.homecontrol.workmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.andrewgiang.homecontrol.App
import com.andrewgiang.homecontrol.api.ApiHolder
import com.andrewgiang.homecontrol.api.AuthManager
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class AuthTokenWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    @Inject
    lateinit var apiHolder: ApiHolder
    @Inject
    lateinit var authManager: AuthManager

    override fun doWork(): Result {
        (applicationContext as App).applicationComponent.inject(this)
        if (authManager.isAuthenticated()) {
            val result = authManager.authToken?.let { token ->
                runBlocking {
                    try {
                        val newToken = apiHolder.api.reauth(token).await()
                        authManager.updateAuthToken(newToken)
                        return@runBlocking Result.SUCCESS
                    } catch (e: Exception) {
                        return@runBlocking Result.RETRY
                    }
                }
            }
            if (result != null) {
                return result
            }
        }
        return Result.SUCCESS
    }

}