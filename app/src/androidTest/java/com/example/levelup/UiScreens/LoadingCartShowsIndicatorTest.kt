package com.example.levelup.UiScreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.example.levelup.ui.screens.LoadingCart
import org.junit.Rule
import org.junit.Test

class LoadingCartShowsIndicatorTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun loading_cart_is_visible() {
        compose.setContent { LoadingCart() }
        compose.onNodeWithTag("cart_loading").assertIsDisplayed()
    }
}

