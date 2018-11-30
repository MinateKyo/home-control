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

package com.andrewgiang.homecontrol.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.andrewgiang.homecontrol.data.database.model.Entity
import com.andrewgiang.homecontrol.data.repo.ActionRepo
import com.andrewgiang.homecontrol.data.repo.EntityRepo
import com.andrewgiang.homecontrol.testDispatchProvider
import com.andrewgiang.homecontrol.testObserver
import com.andrewgiang.homecontrol.ui.Nav
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class AddActionViewModelTest {

    private val mockActionRepo: ActionRepo = mockk()
    private val mockEntityRepo: EntityRepo = mockk()

    private lateinit var subject: AddActionViewModel

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        subject = AddActionViewModel(
            mockActionRepo,
            mockEntityRepo,
            testDispatchProvider()
        )
    }

    @Test
    fun `on bind post values loading then service list`() {
        val domainServiceList = listOf("one.o", "two.d")
        coEvery { mockActionRepo.getDomainServiceList() } returns domainServiceList
        coEvery { mockEntityRepo.refreshStates() } returns emptyList()

        val testObserver = subject.getUiState().testObserver()

        subject.onBind()

        val observedValues = testObserver.observedValues
        assertEquals(AddActionUiModel.Loading, observedValues[0])
        assertEquals(AddActionUiModel.ServiceLoaded(domainServiceList), observedValues[1])
        coVerify { mockEntityRepo.refreshStates() }
    }

    @Test
    fun `selecting domain service will update entity view state`() {
        val element = Entity("id", "on", mapOf())
        coEvery { mockEntityRepo.getEntity(eq("domain")) } returns listOf(element)

        subject.onItemSelected("domain.test")

        assertEquals(subject.getUiState().value!!, AddActionUiModel.EntitiesLoaded(listOf(element), true))
    }

    @Test
    fun `add action will insert action into repo and update view state to shouldFinish true `() {
        coEvery { mockActionRepo.insertAction(any()) } just Runs

        subject.onAddButtonClicked("domain.service", "displayName", true)

        assertEquals(Nav.PopStack, subject.getNavState().value!!)
    }
}