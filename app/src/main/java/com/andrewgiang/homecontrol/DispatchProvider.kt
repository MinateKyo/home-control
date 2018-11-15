package com.andrewgiang.homecontrol

import kotlinx.coroutines.CoroutineDispatcher

data class DispatchProvider(
    val io: CoroutineDispatcher,
    val main: CoroutineDispatcher
)
