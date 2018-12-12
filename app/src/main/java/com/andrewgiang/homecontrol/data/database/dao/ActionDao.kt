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

package com.andrewgiang.homecontrol.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
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

    @Delete
    fun remove(action: Action)

    @Query("SELECT * FROM `Action` WHERE id = :actionId LIMIT 1")
    fun getAction(actionId: Long): Action?
}