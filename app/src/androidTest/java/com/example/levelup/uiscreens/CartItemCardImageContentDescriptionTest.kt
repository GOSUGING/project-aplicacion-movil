package com.example.levelup.uiscreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import com.example.levelup.model.CartItem
import com.example.levelup.ui.screens.CartItemCard
import org.junit.Rule
import org.junit.Test

class CartItemCardImageContentDescriptionTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun image_uses_item_name_as_content_description() {
        val item = CartItem(21, 21, "Mouse Gamer", 350, 1, "/img21")

        compose.setContent { CartItemCard(item = item, onDelete = {}) }

        compose.onNodeWithContentDescription("Mouse Gamer").assertIsDisplayed()
    }
}

