package com.example.levelup.uiscreens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.levelup.ui.screens.HomeScreen
import org.junit.Rule
import org.junit.Test

class HomeScreenSubtitleVisibleTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun subtitle_text_is_visible() {
        compose.setContent { HomeScreen(paddingValues = PaddingValues(), onNavigateToProducts = {}) }
        compose.onNodeWithText("Equipamiento. Poder. Estilo.").assertIsDisplayed()
    }
}

