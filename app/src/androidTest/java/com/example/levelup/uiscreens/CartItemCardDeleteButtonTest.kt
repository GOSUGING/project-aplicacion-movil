package com.example.levelup.UiScreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.levelup.model.CartItem
import com.example.levelup.ui.screens.CartItemCard
import org.junit.Rule
import org.junit.Test

class CartItemCardDeleteButtonTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun delete_button_triggers_callback() {
        var deletes = 0
        val item = CartItem(id = 5, productId = 5, name = "Headset", price = 700, qty = 1, imageUrl = "/img5")

        compose.setContent {
            CartItemCard(item = item, onDelete = { deletes++ })
        }

        compose.onNodeWithText("Headset").assertIsDisplayed()
        compose.onNodeWithTag("delete_btn_5").assertIsDisplayed().performClick()
        assert(deletes == 1)
    }
}

