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

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andrewgiang.homecontrol.data.model.HomeIcon

@Entity
data class Action(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo
    val id: Long = 0,
    @ColumnInfo
    val data: Data,
    @ColumnInfo
    val icon: HomeIcon,
    @ColumnInfo
    val name: String,
    @ColumnInfo
    val isShortcut: Boolean
)

data class Data(
    val entityId: List<String>,
    val domain: String,
    val service: String
) {
    constructor(
        entityId: String,
        domain: String,
        service: String
    ) : this(listOf(entityId), domain, service)
}
