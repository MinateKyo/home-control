package com.andrewgiang.homecontrol.ui

import androidx.lifecycle.ViewModel
import com.andrewgiang.homecontrol.DispatchProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

open class ScopeViewModel constructor(dispatcherProvider: DispatchProvider) : ViewModel(), CoroutineScope {
    override val coroutineContext = Job() + dispatcherProvider.main

    override fun onCleared() {
        coroutineContext.cancel()
    }
}