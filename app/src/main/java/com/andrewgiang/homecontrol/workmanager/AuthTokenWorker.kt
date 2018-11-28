package com.andrewgiang.homecontrol.workmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.andrewgiang.assistantsdk.response.AuthToken
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
            return authManager.authToken?.let {
                result(it)
            } ?: Result.FAILURE
        }
        return Result.SUCCESS
    }

    private fun result(authToken: AuthToken): Result {
        return runBlocking {
            try {
                val newToken = apiHolder.api.reauth(authToken).await()
                authManager.updateAuthToken(newToken)
                Result.SUCCESS
            } catch (e: Exception) {
                Result.RETRY
            }
        }
    }
}