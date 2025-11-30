package com.example.levelup.uiscreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.levelup.model.CartItem
import com.example.levelup.ui.screens.CartItemCard
import org.junit.Rule
import org.junit.Test

class CartItemCardDisplaysTextsTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun renders_qty_and_price_texts() {
        val item = CartItem(3, 3, "Teclado", 500, 2, "/img3")

        compose.setContent { CartItemCard(item = item, onDelete = {}) }

        compose.onNodeWithText("Teclado").assertIsDisplayed()
        compose.onNodeWithText("Cantidad: 2").assertIsDisplayed()
        compose.onNodeWithText("$500").assertIsDisplayed()
    }
}

