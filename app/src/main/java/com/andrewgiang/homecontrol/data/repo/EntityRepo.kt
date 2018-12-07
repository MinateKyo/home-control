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

package com.andrewgiang.homecontrol.data.repo

import com.andrewgiang.homecontrol.DispatchProvider
import com.andrewgiang.homecontrol.api.ApiHolder
import com.andrewgiang.homecontrol.data.database.dao.EntityDao
import com.andrewgiang.homecontrol.data.database.model.Entity
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EntityRepo @Inject constructor(
    private val entityDao: EntityDao,
    private val apiHolder: ApiHolder,
    private val dispatchProvider: DispatchProvider
) {

    suspend fun getEntity(filterDomain: String): List<Entity> {
        return withContext(dispatchProvider.io) {
            entityDao.getEntitiesSync()
                .filter {
                    it.getDomain() == filterDomain || checkGroupForDomain(it, filterDomain)
                }
        }
    }

    private fun checkGroupForDomain(entity: Entity, filterDomain: String): Boolean {
        if (entity.getDomain() != "group") return false
        val groupEntityIds = entity.attributes["entity_id"] as ArrayList<String>
        return groupEntityIds.all {
            it.startsWith(filterDomain)
        }
    }

    suspend fun refreshStates(): List<Long> {
        val entityResponse = apiHolder.api.getStates().await()
        return withContext(dispatchProvider.io) {
            entityDao.insert(entityResponse
                .map { it ->
                    Entity(it.entity_id, it.state, it.attributes)
                })
        }
    }
}