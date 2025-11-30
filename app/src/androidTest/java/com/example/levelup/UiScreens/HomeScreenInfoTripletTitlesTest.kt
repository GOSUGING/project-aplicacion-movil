package com.example.levelup.UiScreens

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

class HomeScreenInfoTripletTitlesTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun info_triplet_titles_visible_after_scroll() {
        compose.setContent { HomeScreen(paddingValues = PaddingValues(), onNavigateToProducts = {}) }

        compose.onNodeWithTag("home_list").performScrollToNode(hasText("¿QUIÉNES SOMOS?"))
        compose.onNodeWithText("¿QUIÉNES SOMOS?").assertIsDisplayed()

        compose.onNodeWithTag("home_list").performScrollToNode(hasText("MISIÓN"))
        compose.onNodeWithText("MISIÓN").assertIsDisplayed()

        compose.onNodeWithTag("home_list").performScrollToNode(hasText("VISIÓN"))
        compose.onNodeWithText("VISIÓN").assertIsDisplayed()
    }
}

