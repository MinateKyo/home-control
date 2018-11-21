package com.andrewgiang.homecontrol.data.database.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andrewgiang.homecontrol.data.model.Icon


@Entity
open class Action(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo
    val id: Long = 0,
    @ColumnInfo
    val data: Data,
    @ColumnInfo
    val icon: Icon,
    @ColumnInfo
    val name: String
)

sealed class Data {

    class AppData : Data()

    data class ServiceData(
        val entityId: String,
        val domain: String,
        val service: String
    ) : Data()
}

