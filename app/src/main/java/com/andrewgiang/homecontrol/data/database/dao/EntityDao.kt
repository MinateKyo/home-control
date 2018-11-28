package com.andrewgiang.homecontrol.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andrewgiang.homecontrol.data.database.model.Entity

@Dao
interface EntityDao {

    @Query("SELECT * FROM Entity ORDER BY entity_id")
    fun getEntities(): LiveData<List<Entity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entities: List<Entity>): List<Long>
}