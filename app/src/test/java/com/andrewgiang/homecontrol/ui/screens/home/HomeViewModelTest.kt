package com.andrewgiang.homecontrol.ui.screens.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.andrewgiang.assistantsdk.Api
import com.andrewgiang.assistantsdk.response.Entity
import com.andrewgiang.homecontrol.ActionShortcutManager
import com.andrewgiang.homecontrol.api.ApiHolder
import com.andrewgiang.homecontrol.api.AuthManager
import com.andrewgiang.homecontrol.data.model.Action
import com.andrewgiang.homecontrol.data.model.AppAction
import com.andrewgiang.homecontrol.data.model.Data
import com.andrewgiang.homecontrol.data.model.Icon
import com.andrewgiang.homecontrol.ui.testDispatchProvider
import io.mockk.*
import junit.framework.Assert.*
import kotlinx.coroutines.Deferred
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.util.concurrent.CountDownLatch

class HomeViewModelTest {
    val mockHolder: ApiHolder = mockk()
    val mockApi: Api = mockk()
    val mockActionShortcutManager: ActionShortcutManager = mockk()
    val mockAuth: AuthManager = mockk(relaxed = true)

    lateinit var subject: HomeViewModel


    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        every { mockHolder.api } returns mockApi
        subject = HomeViewModel(
            mockHolder,
            mockActionShortcutManager,
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


        val expectedData = Data.ServiceData("entity", "domain", "service")
        val action = Action(
            expectedData,
            Icon(MaterialDrawableBuilder.IconValue.IMPORT_ICON),
            "Name"
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
        val downLatch = CountDownLatch(3)
        subject.getViewState().observeForever {
            println(downLatch.count)
            when (downLatch.count) {
                1L -> assertFalse(it.isLoading) // finish loading
                2L -> assertTrue(it.isLoading)  // loading from api
                3L -> assertFalse(it.isLoading) // initial state
            }
            downLatch.countDown()
        }
        subject.onClick(action)
        downLatch.await()

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
        every { mockActionShortcutManager.fromJson(eq("valid_data")) } returns null

        subject.onShortcutClick("valid_data")

        verify { mockActionShortcutManager.fromJson(eq("valid_data")) }
        verify { mockApi wasNot Called }
    }

    @Test
    fun `onShortcutClick with valid data will invoke service data`() {

        val expectedData = Data.ServiceData("entity", "domain", "service")
        every { mockActionShortcutManager.fromJson(eq("valid_data")) } returns expectedData

        subject.onShortcutClick("valid_data")

        verify { mockActionShortcutManager.fromJson(eq("valid_data")) }
        verify { mockApi.service(eq(expectedData.entityId), eq(expectedData.domain), eq(expectedData.service)) }

    }
}