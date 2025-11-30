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
import com.example.levelup.ui.screens.ProfileScreen
import com.example.levelup.viewmodel.ProfileViewModel
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class ProfileScreenPreferencesTextsTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun preferences_texts_are_visible() {
        val repo = mockk<UserRepository>()
        val session = mockk<SessionManager>()
        every { session.getCurrentUser() } returns UserSession(1L, "Carol", "carol@example.com", "USER")
        val vm = ProfileViewModel(repo, session)

        compose.setContent {
            val nav = rememberNavController()
            ProfileScreen(paddingValues = PaddingValues(), navController = nav, vm = vm)
        }

        compose.onNodeWithText("Preferencias").assertIsDisplayed()
        compose.onNodeWithText("Recibir newsletter").assertIsDisplayed()
        compose.onNodeWithText("Recibir promociones").assertIsDisplayed()
    }
}

