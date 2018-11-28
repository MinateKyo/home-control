package com.andrewgiang.homecontrol.ui.screens.home

import com.andrewgiang.homecontrol.data.database.model.Action

data class HomeState(
    val actionIds: List<Action> = emptyList(),
    val isLoading: Boolean = false
)