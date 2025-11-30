package com.example.levelup.UiScreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.example.levelup.data.UserRepository
import com.example.levelup.data.session.SessionManager
import com.example.levelup.viewmodel.ProfileViewModel
import androidx.compose.foundation.layout.PaddingValues
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class ProfileScreenButtonsTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun showsSave_andLogoutButtons() {
        val repo = mockk<UserRepository>()
        val session = mockk<SessionManager>()
        every { session.getCurrentUser() } returns null
        val vm = ProfileViewModel(repo, session)

        compose.setContent {
            val nav = rememberNavController()
            com.example.levelup.ui.screens.ProfileScreen(
                navController = nav,
                vm = vm,
                paddingValues = PaddingValues() //
            )
        }

        compose.onNodeWithText("Guardar").assertIsDisplayed()
        compose.onNodeWithText("Cerrar sesión").assertIsDisplayed()
    }
}

