package com.example.levelup.uiscreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.levelup.model.CartItem
import com.example.levelup.ui.screens.CartItemCard
import org.junit.Rule
import org.junit.Test

class CartItemCardDeleteLabelTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun delete_button_label_x_is_visible() {
        val item = CartItem(20, 20, "Monitor", 1200, 1, "/img20")
        compose.setContent { CartItemCard(item = item, onDelete = {}) }
        compose.onNodeWithText("X").assertIsDisplayed()
    }
}

