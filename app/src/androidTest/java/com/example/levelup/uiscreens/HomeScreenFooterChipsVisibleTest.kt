package com.example.levelup.uiscreens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToNode
import com.example.levelup.ui.screens.HomeScreen
import org.junit.Rule
import org.junit.Test

class HomeScreenFooterChipsVisibleTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun footer_chips_labels_visible_after_scroll() {
        compose.setContent { HomeScreen(paddingValues = PaddingValues(), onNavigateToProducts = {}) }

        compose.onNodeWithTag("home_list").performScrollToNode(hasText("PayPal"))
        compose.onNodeWithText("PayPal").assertIsDisplayed()

        compose.onNodeWithTag("home_list").performScrollToNode(hasText("Visa"))
        compose.onNodeWithText("Visa").assertIsDisplayed()
    }
}

