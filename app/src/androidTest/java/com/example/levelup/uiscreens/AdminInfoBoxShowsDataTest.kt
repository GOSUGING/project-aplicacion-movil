package com.example.levelup.UiScreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.levelup.ui.screens.AdminInfoBox
import org.junit.Rule
import org.junit.Test

class AdminInfoBoxShowsDataTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun admin_info_box_shows_name_and_email() {
        compose.setContent { AdminInfoBox(name = "Alice", email = "alice@example.com") }
        compose.onNodeWithText("Administrador").assertIsDisplayed()
        compose.onNodeWithText("Alice").assertIsDisplayed()
        compose.onNodeWithText("Correo").assertIsDisplayed()
        compose.onNodeWithText("alice@example.com").assertIsDisplayed()
    }
}

