package com.andrewgiang.homecontrol.ui.screens.add.action

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.andrewgiang.homecontrol.data.database.model.Entity
import com.andrewgiang.homecontrol.data.repo.ActionRepo
import com.andrewgiang.homecontrol.data.repo.EntityRepo
import com.andrewgiang.homecontrol.ui.testDispatchProvider
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class AddActionViewModelTest {

    private val mockActionRepo: ActionRepo = mockk()
    private val mockEntityRepo: EntityRepo = mockk()

    lateinit var subject: AddActionViewModel

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        subject = AddActionViewModel(mockActionRepo, mockEntityRepo, testDispatchProvider())
    }

    @Test
    fun `load services post values domain service list`() {
        val domainServiceList = listOf("one.o", "two.d")
        coEvery { mockActionRepo.getDomainServiceList() } returns domainServiceList
        subject.loadServices()
        assertEquals(domainServiceList, subject.getViewState().value!!.listDomainService)
    }

    @Test
    fun `selecting domain service will update entity view state`() {
        val element = Entity("id", "on", mapOf())
        coEvery { mockEntityRepo.getEntity(eq("domain")) } returns listOf(element)
        subject.onItemSelected("domain.test")

        assertEquals(subject.getEntities().value!!, listOf(element))
    }

    @Test
    fun `add action will insert action into repo and update view state to shouldFinish true `() {

        coEvery { mockActionRepo.insertAction(any()) } just Runs

        subject.addAction("domain.service", listOf("entity_id"), "displayName", true)

        assertTrue(subject.getViewState().value!!.shouldFinish)
    }
}