package com.example.levelup.uiscreens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import org.junit.Rule
import org.junit.Test

class CategoriesScreenShowsCategoryNamesTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun renders_all_category_names() {
        compose.setContent {
            val nav = rememberNavController()
            com.example.levelup.ui.screens.CategoriesScreen(
                navController = nav,
                paddingValues = PaddingValues()
            )
        }

        compose.onNodeWithText("Consolas").assertIsDisplayed()
        compose.onNodeWithText("Juegos").assertIsDisplayed()
        compose.onNodeWithText("Accesorios").assertIsDisplayed()
        compose.onNodeWithText("Ropa Gamer").assertIsDisplayed()
        compose.onNodeWithText("Tarjetas Graficas").assertIsDisplayed()
    }
}

