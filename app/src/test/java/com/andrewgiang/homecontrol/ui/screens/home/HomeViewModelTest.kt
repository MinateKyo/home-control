package com.andrewgiang.homecontrol.ui.screens.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.andrewgiang.assistantsdk.Api
import com.andrewgiang.assistantsdk.response.Entity
import com.andrewgiang.homecontrol.ActionShortcutManager
import com.andrewgiang.homecontrol.api.ApiHolder
import com.andrewgiang.homecontrol.api.AuthManager
import com.andrewgiang.homecontrol.data.database.model.Action
import com.andrewgiang.homecontrol.data.database.model.Data
import com.andrewgiang.homecontrol.data.model.AppAction
import com.andrewgiang.homecontrol.data.model.Icon
import com.andrewgiang.homecontrol.data.repo.ActionRepo
import com.andrewgiang.homecontrol.ui.testDispatchProvider
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Deferred
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class HomeViewModelTest {
    val mockHolder: ApiHolder = mockk()
    val mockApi: Api = mockk()
    val mockActionShortcutManager: ActionShortcutManager = mockk()
    val mockAuth: AuthManager = mockk(relaxed = true)
    val mockActionRepo: ActionRepo = mockk()

    lateinit var subject: HomeViewModel

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        every { mockHolder.api } returns mockApi
        subject = HomeViewModel(
            mockHolder,
            mockActionShortcutManager,
            mockActionRepo,
            mockAuth,
            testDispatchProvider()
        )
    }

    @Test
    fun `onClick app action will post app action`() {
        val action = AppAction.FullScreen()
        subject.onClick(action)
        assertEquals(action, subject.getAppActions().value)
    }

    @Test
    fun `onClick with service data will invoke api request and update in progress state`() {

        val mutableLiveData = MutableLiveData<List<Action>>()
        every { mockActionRepo.actionData() } returns mutableLiveData
        val expectedData = Data.ServiceData("entity", "domain", "service")
        val action = Action(
            data = expectedData,
            icon = Icon(MaterialDrawableBuilder.IconValue.IMPORT_ICON),
            name = "Name",
            isShortcut = false
        )

        val deferred = mockk<Deferred<List<Entity>>>()
        coEvery { deferred.await() } returns mockk()
        coEvery {
            mockApi.service(
                eq(expectedData.entityId),
                eq(expectedData.domain),
                eq(expectedData.service)
            )
        } returns deferred
        val downLatch = CountDownLatch(2)
        subject.getViewState().observeForever {
            when (downLatch.count) {
                1L -> assertFalse(it.isLoading) // finish loading
                2L -> assertTrue(it.isLoading) // loading from api
            }
            downLatch.countDown()
        }
        subject.onClick(action)
        downLatch.await(3, TimeUnit.SECONDS)

        verify { mockApi.service(eq(expectedData.entityId), eq(expectedData.domain), eq(expectedData.service)) }
    }

    @Test
    fun `onShortcutClick with null data will do nothing`() {
        subject.onShortcutClick(null)
        verify { mockActionShortcutManager wasNot Called }
        verify { mockApi wasNot Called }
    }

    @Test
    fun `onShortcutClick with invalid data will do nothing`() {
        every { mockActionShortcutManager.parseShortcutData(eq("valid_data")) } returns null

        subject.onShortcutClick("valid_data")

        verify { mockActionShortcutManager.parseShortcutData(eq("valid_data")) }
        verify { mockApi wasNot Called }
    }

    @Test
    fun `onShortcutClick with valid data will invoke service data`() {

        val expectedData = Data.ServiceData("entity", "domain", "service")
        every { mockActionShortcutManager.parseShortcutData(eq("valid_data")) } returns expectedData

        subject.onShortcutClick("valid_data")

        verify { mockActionShortcutManager.parseShortcutData(eq("valid_data")) }
        verify { mockApi.service(eq(expectedData.entityId), eq(expectedData.domain), eq(expectedData.service)) }
    }
}