package com.example.levelup.UiScreens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.performScrollToNode
import com.example.levelup.ui.screens.HomeScreen
import org.junit.Rule
import org.junit.Test

class HomeScreenTitleFooterTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun title_and_footer_texts_are_visible() {
        compose.setContent {
            HomeScreen(paddingValues = PaddingValues(), onNavigateToProducts = {})
        }
        compose.onNodeWithText("LEVEL-UP GAMER").assertIsDisplayed()
        compose.onNodeWithText("Blogs Destacados").assertIsDisplayed()
        compose.onNodeWithTag("home_list").performScrollToNode(hasText("Aceptamos todo medio de pago"))
        compose.onNodeWithTag("home_list").performScrollToNode(hasText("© 2025 Level-Up Gamer"))
        compose.onNodeWithText("Aceptamos todo medio de pago").assertIsDisplayed()
        compose.onNodeWithText("© 2025 Level-Up Gamer").assertIsDisplayed()
    }
}
