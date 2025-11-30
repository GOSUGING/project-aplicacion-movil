package com.example.levelup.uiscreens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import org.junit.Rule
import org.junit.Test

class CategoriesScreenVerTodosButtonVisibleTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun shows_ver_todos_button() {
        compose.setContent {
            val nav = rememberNavController()
            com.example.levelup.ui.screens.CategoriesScreen(
                navController = nav,
                paddingValues = PaddingValues()
            )
        }

        compose.onNodeWithText("Ver todos").assertIsDisplayed()
    }
}

