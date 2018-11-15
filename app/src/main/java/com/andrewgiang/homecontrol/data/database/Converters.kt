package com.andrewgiang.homecontrol.data.database

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types


class Converters {
    var mapType = Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java)
    val moshi: Moshi = Moshi.Builder().build()
    val mapAdapter: JsonAdapter<Map<String, Any>> = moshi.adapter(mapType)


    @TypeConverter
    fun mapFromJson(json: String): Map<String, Any>? {
        return mapAdapter.fromJson(json)
    }

    @TypeConverter
    fun toJson(map: Map<String, Any>?): String {
        return mapAdapter.toJson(map)
    }


}

