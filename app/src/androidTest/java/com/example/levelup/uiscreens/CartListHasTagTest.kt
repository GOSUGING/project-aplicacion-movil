package com.example.levelup.uiscreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.example.levelup.model.CartItem
import com.example.levelup.ui.screens.CartListContent
import org.junit.Rule
import org.junit.Test

class CartListHasTagTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun cart_list_tag_present() {
        val items = listOf(CartItem(1,1,"Mouse",300,1,"/img1"))
        compose.setContent { CartListContent(items = items, onDelete = {}, onCheckout = {}) }
        compose.onNodeWithTag("cart_list").assertIsDisplayed()
    }
}

