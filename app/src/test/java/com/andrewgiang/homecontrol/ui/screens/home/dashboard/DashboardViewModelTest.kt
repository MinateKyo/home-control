package com.andrewgiang.homecontrol.ui.screens.home.dashboard

import androidx.lifecycle.LiveData
import com.andrewgiang.homecontrol.DispatchProvider
import com.andrewgiang.homecontrol.data.EntityRepository
import com.andrewgiang.homecontrol.data.database.model.Entity
import com.andrewgiang.homecontrol.ui.testDispatchProvider
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class DashboardViewModelTest {


    val dispatchProvider: DispatchProvider = testDispatchProvider()

    val entityRepository: EntityRepository = mockk(relaxed = true)

    val subject: DashboardViewModel = DashboardViewModel(dispatchProvider, entityRepository)


    @Test
    fun test_refresh_data_will_invoke_repository_refresh() {
        subject.refreshData()
        coVerify {
            entityRepository.refreshStates()
        }
    }


    @Test
    fun test_repository_return_observed_live_data() {
        val liveData = mockk<LiveData<List<Entity>>>()
        every { entityRepository.observeEntities() } returns liveData
        assertEquals(subject.getEntities(), liveData)

    }


}