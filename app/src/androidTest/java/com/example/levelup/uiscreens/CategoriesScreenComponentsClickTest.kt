package com.example.levelup.uiscreens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.example.levelup.ui.screens.CategoriesScreen
import org.junit.Rule
import org.junit.Test

class CategoriesScreenComponentsClickTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun categories_and_actions_render_and_click() {
        compose.setContent {
            val nav = rememberNavController()
            CategoriesScreen(navController = nav, paddingValues = PaddingValues())
        }

        compose.onNodeWithText("Categorías").assertIsDisplayed()
        compose.onNodeWithText("Consolas").assertIsDisplayed()
        compose.onNodeWithText("Juegos").assertIsDisplayed()
        compose.onNodeWithText("Accesorios").assertIsDisplayed()
        compose.onNodeWithText("Ropa Gamer").assertIsDisplayed()
        compose.onNodeWithText("Tarjetas Graficas").assertIsDisplayed()
        compose.onNodeWithText("Ver todos").assertIsDisplayed()
    }
}
