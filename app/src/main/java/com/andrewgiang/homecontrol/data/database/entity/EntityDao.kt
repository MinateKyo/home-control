package com.andrewgiang.homecontrol.data.database.entity

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EntityDao {

    @Query("SELECT * From Entity ORDER BY entity_id")
    fun getEntities(): LiveData<List<Entity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entities: List<Entity>)

}
