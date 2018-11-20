package com.andrewgiang.homecontrol.ui

import com.andrewgiang.homecontrol.DispatchProvider
import kotlinx.coroutines.Dispatchers

fun testDispatchProvider() = DispatchProvider(Dispatchers.Unconfined, Dispatchers.Unconfined)
