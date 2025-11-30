package com.example.levelup.uiscreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.levelup.model.CartItem
import com.example.levelup.ui.screens.CartListContent
import org.junit.Rule
import org.junit.Test

class CartListContentDifferentSubtotalTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun subtotal_renders_correctly_for_different_items() {
        val items = listOf(
            CartItem(id = 10, productId = 10, name = "Mouse", price = 300, qty = 3, imageUrl = "/img1"),
            CartItem(id = 11, productId = 11, name = "Teclado", price = 400, qty = 1, imageUrl = "/img2")
        )

        compose.setContent { CartListContent(items = items, onDelete = {}, onCheckout = {}) }

        compose.onNodeWithText("Subtotal: $1300").assertIsDisplayed()
    }
}

