package com.andrewgiang.homecontrol.api

import com.andrewgiang.assistantsdk.response.AuthToken
import com.andrewgiang.homecontrol.data.AuthPrefs
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthManagerTest {

    val authToken: AuthToken = mockk()

    val mockPrefs: AuthPrefs = mockk()

    @Test
    fun isAuthenticated_if_host_and_token_available() {

        mockAuthenticated()

        assertTrue(subject().isAuthenticated())
    }

    @Test
    fun isAuthenticated_false_if_token_not_available() {
        every { mockPrefs.getHostUrl() } returns "some host"
        every { mockPrefs.getAuthToken() } returns null
        assertFalse(subject().isAuthenticated())
    }

    @Test
    fun isAuthenticated_false_if_host_url_is_notAvailable() {
        every { mockPrefs.getHostUrl() } returns null
        every { mockPrefs.getAuthToken() } returns authToken
        assertFalse(subject().isAuthenticated())
    }

    @Test
    fun update_when_authenticated_will_use_new_token_but_retain_current_refresh_token_which_does_not_change() {
        mockAuthenticated()
        every { authToken.access_token } returns "current"
        every { authToken.expires_in } returns 1
        every { authToken.refresh_token } returns "real_refresh"
        every { authToken.token_type } returns "current_type"
        val subject = subject()

        val newToken = AuthToken("newToken", 2, "", "new_type")
        subject.updateAuthToken(newToken)

        val expectedToken = AuthToken("newToken", 2, "real_refresh", "new_type")
        assertEquals(
            expectedToken,
            subject.authToken
        )

        verify {
            mockPrefs.setAuthToken(eq(expectedToken))
        }
    }

    @Test
    fun update_when_not_authenticated_will_update_manager_token_and_save_to_prefs() {
        mockUnauth()

        val token = AuthToken("expected", 1800, "refresh", "type")
        val subject = subject()
        subject.updateAuthToken(token)

        verify { mockPrefs.setAuthToken(eq(token)) }
        assertEquals(token, subject.authToken)
    }

    private fun mockUnauth() {
        every { mockPrefs.getHostUrl() } returns null
        every { mockPrefs.getAuthToken() } returns null
    }

    @Test
    fun setHost_will_update_shared_prefs() {
        mockUnauth()
        subject().setHost("any")
        verify { mockPrefs.setHostUrl(eq("any")) }
    }

    private fun mockAuthenticated() {
        every { mockPrefs.getHostUrl() } returns "some host"
        every { mockPrefs.getAuthToken() } returns authToken
    }

    fun subject(): AuthManager {
        return AuthManager(mockPrefs)
    }
}