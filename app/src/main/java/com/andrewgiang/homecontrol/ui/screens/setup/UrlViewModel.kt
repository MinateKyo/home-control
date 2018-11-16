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

    private fun getAuthToken(code: String) = launch {
        try {
            val authToken = holder.api.initialAuth(code).await()
            authManager.updateAuthToken(authToken)
            data.postValue(
                UrlState(authState = AuthState.AUTHENTICATED)
            )
        } catch (e: Exception) {
            data.postValue(UrlState(isLoading = false, errorMessage = "Unable to authenticate"))
        }

    }

    fun onAppLinkRedirect(code: String?) {
        if (code != null) {
            getAuthToken(code)
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