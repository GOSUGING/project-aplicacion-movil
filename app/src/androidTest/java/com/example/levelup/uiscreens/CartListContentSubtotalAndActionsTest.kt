package com.example.levelup.uiscreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.onNodeWithTag
import com.example.levelup.model.CartItem
import com.example.levelup.ui.screens.CartListContent
import org.junit.Rule
import org.junit.Test

class CartListContentSubtotalAndActionsTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun subtotal_renders_and_actions_fire() {
        var deletes = 0
        var checkouts = 0
        val items = listOf(
            CartItem(id = 1, productId = 1, name = "Mouse", price = 300, qty = 2, imageUrl = "/img1"),
            CartItem(id = 2, productId = 2, name = "Teclado", price = 500, qty = 1, imageUrl = "/img2")
        )

        compose.setContent {
            CartListContent(items = items, onDelete = { deletes++ }, onCheckout = { checkouts++ })
        }

        compose.onNodeWithText("Subtotal: $1100").assertIsDisplayed()
        compose.onNodeWithText("Proceder al pago").assertIsDisplayed().performClick()
        compose.onNodeWithTag("delete_btn_1").assertIsDisplayed().performClick()

        assert(checkouts == 1)
        assert(deletes == 1)
    }
}
