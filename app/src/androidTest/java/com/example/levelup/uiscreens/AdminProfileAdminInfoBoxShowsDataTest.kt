package com.example.levelup.uiscreens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.example.levelup.data.UserRepository
import com.example.levelup.data.session.SessionManager
import com.example.levelup.data.session.UserSession
import com.example.levelup.viewmodel.ProfileViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AdminProfileAdminInfoBoxShowsDataTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun admin_profile_info_box_shows_name_and_email() = runTest(UnconfinedTestDispatcher()) {
        val repo = mockk<UserRepository>()
        val session = mockk<SessionManager>()
        every { session.getCurrentUser() } returns UserSession(5, "Admin Five", "admin5@example.com", "ADMIN")

        val vm = ProfileViewModel(repo, session)

        compose.setContent {
            val nav = rememberNavController()
            com.example.levelup.ui.screens.AdminProfileScreen(
                paddingValues = PaddingValues(),
                navController = nav,
                vm = vm
            )
        }

        advanceUntilIdle()

        compose.onNodeWithText("Administrador").assertIsDisplayed()
        compose.onNodeWithText("Admin Five").assertIsDisplayed()
        compose.onNodeWithText("Correo").assertIsDisplayed()
        compose.onNodeWithText("admin5@example.com").assertIsDisplayed()
    }
}

