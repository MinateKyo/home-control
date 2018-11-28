package com.andrewgiang.homecontrol.data.database

import androidx.room.TypeConverter
import com.andrewgiang.homecontrol.data.database.model.Data
import com.andrewgiang.homecontrol.data.model.Icon
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder

class Converters {
    private var mapType = Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java)
    private val moshi: Moshi = Moshi.Builder().build()
    private val mapAdapter: JsonAdapter<Map<String, Any>> = moshi.adapter(mapType)
    private val appDataAdapter: JsonAdapter<Data.AppData> = moshi.adapter(Data.AppData::class.java)
    private val serviceDataAdapter: JsonAdapter<Data.ServiceData> = moshi.adapter(Data.ServiceData::class.java)
    private val iconAdapter: JsonAdapter<Icon> = moshi.adapter(Icon::class.java)

    @TypeConverter
    fun mapFromJson(json: String): Map<String, Any>? {
        return mapAdapter.fromJson(json)
    }

    @TypeConverter
    fun mapToJson(map: Map<String, Any>?): String {
        return mapAdapter.toJson(map)
    }

    @TypeConverter
    fun iconToString(iconValue: MaterialDrawableBuilder.IconValue): String {
        return iconValue.name
    }

    @TypeConverter
    fun icomFromString(name: String): MaterialDrawableBuilder.IconValue {
        return MaterialDrawableBuilder.IconValue.valueOf(name)
    }

    @TypeConverter
    fun dataToString(data: Data): String {
        return when (data) {
            is Data.AppData -> "app;${appDataAdapter.toJson(data)}"
            is Data.ServiceData -> "data;${serviceDataAdapter.toJson(data)}"
        }
    }

    @TypeConverter
    fun dataFromMetaJsonString(metaJson: String): Data? {
        val split = metaJson.split(";", limit = 2)
        assert(split.size == 2)
        val type = split[0]
        val json = split[1]
        return when (type) {
            "app" -> appDataAdapter.fromJson(json)
            "data" -> serviceDataAdapter.fromJson(json)
            else -> {
                throw IllegalStateException("Illegal type conversion $type")
            }
        }
    }

    @TypeConverter
    fun iconToString(icon: Icon): String {
        return iconAdapter.toJson(icon)
    }

    @TypeConverter
    fun iconFromJson(json: String): Icon? {
        return iconAdapter.fromJson(json)
    }
}