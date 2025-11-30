package com.example.levelup.UiScreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.example.levelup.ui.screens.EmptyCart
import org.junit.Rule
import org.junit.Test

class EmptyCartShowsMessageTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun empty_cart_message_is_visible() {
        compose.setContent { EmptyCart() }
        compose.onNodeWithTag("cart_empty").assertIsDisplayed()
        compose.onNodeWithText("Tu carrito está vacío").assertIsDisplayed()
    }
}

