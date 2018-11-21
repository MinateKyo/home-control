package com.andrewgiang.homecontrol.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.andrewgiang.homecontrol.data.database.dao.EntityDao
import com.andrewgiang.homecontrol.data.database.model.Action
import com.andrewgiang.homecontrol.data.database.model.Entity


@Database(entities = [Entity::class, Action::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun entityDao(): EntityDao

}