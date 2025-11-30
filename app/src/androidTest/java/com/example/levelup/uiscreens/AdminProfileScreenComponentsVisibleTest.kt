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
class AdminProfileScreenComponentsVisibleTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun admin_profile_shows_cards_and_buttons() = runTest(UnconfinedTestDispatcher()) {
        val repo = mockk<UserRepository>()
        val session = mockk<SessionManager>()
        every { session.getCurrentUser() } returns UserSession(1, "Admin", "admin@example.com", "ADMIN")

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

        compose.onNodeWithText("ADMIN PANEL").assertIsDisplayed()
        compose.onNodeWithText("Gestión de Usuarios").assertIsDisplayed()
        compose.onNodeWithText("Gestión de Productos / Stock").assertIsDisplayed()
        compose.onNodeWithText("Historial de Ventas").assertIsDisplayed()
        compose.onNodeWithText("Cerrar Panel Admin").assertIsDisplayed()
        compose.onNodeWithText("Cerrar Sesión").assertIsDisplayed()
    }
}

