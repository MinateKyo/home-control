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

package com.andrewgiang.homecontrol.data.database

import androidx.room.TypeConverter
import com.andrewgiang.homecontrol.data.database.model.Data
import com.andrewgiang.homecontrol.data.model.HomeIcon
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class Converters {
    private var mapType = Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java)
    private val moshi: Moshi = Moshi.Builder().build()
    private val mapAdapter: JsonAdapter<Map<String, Any>> = moshi.adapter(mapType)
    private val serviceDataAdapter: JsonAdapter<Data> = moshi.adapter(Data::class.java)
    private val homeIconAdapter: JsonAdapter<HomeIcon> = moshi.adapter(HomeIcon::class.java)

    @TypeConverter
    fun mapFromJson(json: String): Map<String, Any>? {
        return mapAdapter.fromJson(json)
    }

    @TypeConverter
    fun mapToJson(map: Map<String, Any>?): String {
        return mapAdapter.toJson(map)
    }

    @TypeConverter
    fun dataToString(data: Data): String {
        return serviceDataAdapter.toJson(data)
    }

    @TypeConverter
    fun dataFromJsonString(json: String): Data? {
        return serviceDataAdapter.fromJson(json)
    }

    @TypeConverter
    fun iconToString(icon: HomeIcon): String {
        return homeIconAdapter.toJson(icon)
    }

    @TypeConverter
    fun iconFromJson(json: String): HomeIcon? {
        return homeIconAdapter.fromJson(json)
    }
}