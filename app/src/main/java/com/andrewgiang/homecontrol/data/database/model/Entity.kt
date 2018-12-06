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

package com.andrewgiang.homecontrol.data.database.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
@Entity
data class Entity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "entity_id")
    val entity_id: String,

    @ColumnInfo(name = "state")
    val state: String,

    @ColumnInfo(name = "attributes")
    val attributes: @RawValue Map<String, Any>
) : Parcelable {

    fun getDomain(): String {
        return entity_id.split(".").first()
    }

    fun getFriendlyName(): String {
        val name = attributes["friendly_name"]
        return when (name) {
            is String -> name
            else -> entity_id
        }
    }
}