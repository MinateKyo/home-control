package com.andrewgiang.homecontrol.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.andrewgiang.homecontrol.data.database.model.Action

@Dao
interface ActionDao {

    @Insert
    fun insertAction(action: Action)

    @Query("SELECT * FROM `Action` ")
    fun getActions(): LiveData<List<Action>>

    @Query("SELECT * FROM `Action` ")
    fun getActionsBlocking(): List<Action>
}