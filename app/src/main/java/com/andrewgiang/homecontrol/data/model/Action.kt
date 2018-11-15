package com.andrewgiang.homecontrol.data.model

import com.andrewgiang.assistantsdk.request.Service


data class Action(
    val entityId: String,
    val service: Service,
    val icon: Icon,
    val name: String
)