package com.andrewgiang.homecontrol.data.database.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Entity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "entity_id")
    val entity_id: String,

    @ColumnInfo(name = "state")
    val state: String,

    @ColumnInfo(name = "attributes")
    val attributes: Map<String, Any>

)