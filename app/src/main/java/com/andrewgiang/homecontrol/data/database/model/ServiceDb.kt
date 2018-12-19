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
import com.andrewgiang.assistantsdk.response.Service
import com.andrewgiang.assistantsdk.response.ServiceInfo

@Entity
data class ServiceDb(
    @PrimaryKey
    @ColumnInfo
    @NonNull
    val domain: String,
    @ColumnInfo
    val services: Map<String, ServiceInfo>
) {
    companion object {
        fun create(service: Service): ServiceDb {
            return ServiceDb(service.domain, service.services)
        }
    }
}