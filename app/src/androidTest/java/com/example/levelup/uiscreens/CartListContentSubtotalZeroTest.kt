package com.example.levelup.uiscreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.levelup.ui.screens.CartListContent
import org.junit.Rule
import org.junit.Test

class CartListContentSubtotalZeroTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun subtotal_zero_with_empty_items() {
        compose.setContent { CartListContent(items = emptyList(), onDelete = {}, onCheckout = {}) }
        compose.onNodeWithText("Subtotal: $0").assertIsDisplayed()
    }
}

