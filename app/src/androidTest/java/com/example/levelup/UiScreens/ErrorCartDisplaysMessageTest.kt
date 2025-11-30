package com.example.levelup.UiScreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.example.levelup.ui.screens.ErrorCart
import org.junit.Rule
import org.junit.Test

class ErrorCartDisplaysMessageTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun error_cart_shows_message() {
        compose.setContent { ErrorCart("Error crítico") }
        compose.onNodeWithTag("cart_error").assertIsDisplayed()
        compose.onNodeWithText("Error crítico").assertIsDisplayed()
    }
}

