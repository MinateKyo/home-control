package com.andrewgiang.homecontrol.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.andrewgiang.homecontrol.data.database.entity.Entity
import com.andrewgiang.homecontrol.data.database.entity.EntityDao


@Database(entities = arrayOf(Entity::class), version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun entityDao(): EntityDao

}