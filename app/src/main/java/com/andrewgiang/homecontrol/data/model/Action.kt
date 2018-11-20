package com.andrewgiang.homecontrol.data.model


open class Action(
    val data: Data,
    val icon: Icon,
    val name: String
)


sealed class Data {
    class AppData : Data()
    data class ServiceData(val entityId: String, val domain: String, val service: String) : Data()
}

