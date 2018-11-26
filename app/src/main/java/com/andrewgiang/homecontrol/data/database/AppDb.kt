package com.andrewgiang.homecontrol.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.andrewgiang.homecontrol.R
import com.andrewgiang.homecontrol.data.database.dao.ActionDao
import com.andrewgiang.homecontrol.data.database.dao.EntityDao
import com.andrewgiang.homecontrol.data.database.model.Action
import com.andrewgiang.homecontrol.data.database.model.Data
import com.andrewgiang.homecontrol.data.database.model.Entity
import com.andrewgiang.homecontrol.data.model.Icon
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder


@Database(entities = [Entity::class, Action::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun entityDao(): EntityDao
    abstract fun actionDao(): ActionDao

}

val hardCodedActions = mutableListOf(
    Action(
        data = Data.ServiceData(
            "group.all_lights",
            "light",
            "turn_off"
        ),
        icon = Icon(
            MaterialDrawableBuilder.IconValue.LIGHTBULB,
            R.color.darkblue_600, R.color.darkblue_100
        ),
        name = "Off",
        isShortcut = true
    ),
    Action(
        data = Data.ServiceData(
            "group.all_lights",
            "light",
            "turn_on"
        ),
        icon = Icon(
            MaterialDrawableBuilder.IconValue.LIGHTBULB,
            R.color.yellow_600, R.color.yellow_100
        ),
        name = "On",
        isShortcut = true

    )
)