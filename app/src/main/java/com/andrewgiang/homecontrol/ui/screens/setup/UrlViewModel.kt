/*
 * Copyright 2018 Andrew Giang
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.andrewgiang.homecontrol.ui.screens.setup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andrewgiang.homecontrol.DispatchProvider
import com.andrewgiang.homecontrol.api.ApiHolder
import com.andrewgiang.homecontrol.api.AuthManager
import com.andrewgiang.homecontrol.ui.IntentCreator
import com.andrewgiang.homecontrol.ui.ScopeViewModel
import kotlinx.coroutines.launch
import okhttp3.HttpUrl
import timber.log.Timber
import javax.inject.Inject

class UrlViewModel @Inject constructor(
    private val intentCreator: IntentCreator,
    private val holder: ApiHolder,
    private val authManager: AuthManager,
    dispatchProvider: DispatchProvider
) : ScopeViewModel(dispatchProvider) {

    private val data: MutableLiveData<UrlState> = MutableLiveData()

    fun getData(): LiveData<UrlState> {
        return data
    }

    fun onNextClick(urlText: String) {
        val httpUrl = HttpUrl.parse(urlText)
        if (httpUrl != null) {
            authManager.setHost(httpUrl.toString())
            intentCreator.sendAuthorizeIntent(httpUrl)
            data.postValue(UrlState(isLoading = true))
        } else {
            data.postValue(UrlState(errorMessage = "Invalid Url : $urlText"))
        }
    }

    private fun initialLaunch(code: String) = launch {
        try {
            val authToken = holder.api.initialAuth(code).await()
            authManager.updateAuthToken(authToken)
            data.postValue(
                UrlState(authState = AuthState.AUTHENTICATED)
            )
        } catch (e: Exception) {
            Timber.e(e)
            data.postValue(UrlState(isLoading = false, errorMessage = "Unable to authenticate"))
        }
    }

    fun onAppLinkRedirect(code: String?) {
        if (code != null) {
            initialLaunch(code)
        }
    }
}

data class UrlState(
    val isLoading: Boolean = false,
    val authState: AuthState = AuthState.UNAUTHENTICATED,
    val errorMessage: String? = null
)

enum class AuthState {
    UNAUTHENTICATED,
    AUTHENTICATED
}