package com.example.levelup

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class MainActivityNavigationTest {
    @get:Rule
    val compose = createAndroidComposeRule<MainActivity>()

    @Test
    fun menuNavigatesToCategoriesAndProducts() {
        // Open menu and go to Categorías
        compose.onNodeWithContentDescription("Menu").assertIsDisplayed()
        compose.onNodeWithContentDescription("Menu").performClick()
        compose.onNodeWithText("Categorías").assertIsDisplayed()
        compose.onNodeWithText("Categorías").performClick()
        compose.onNodeWithText("Categorías").assertIsDisplayed()

        // Open menu and go to Productos
        compose.onNodeWithContentDescription("Menu").performClick()
        compose.onNodeWithText("Productos").assertIsDisplayed()
        compose.onNodeWithText("Productos").performClick()
        compose.onNodeWithText("Todos los Productos").assertIsDisplayed()
    }

    @Test
    fun profileIconNavigatesToLoginThenRegister() {
        // With no session, tapping profile goes to Login
        compose.onNodeWithContentDescription("Perfil").assertIsDisplayed()
        compose.onNodeWithContentDescription("Perfil").performClick()
        compose.onNodeWithText("INICIAR SESIÓN").assertIsDisplayed()

        // From login go to register
        compose.onNodeWithText("¿No tienes cuenta? Regístrate").assertIsDisplayed()
        compose.onNodeWithText("¿No tienes cuenta? Regístrate").performClick()
        compose.onNodeWithText("CREAR CUENTA").assertIsDisplayed()
    }

    @Test
    fun menuLoginAndRegisterEntriesWork() {
        // Open menu and navigate to Login
        compose.onNodeWithContentDescription("Menu").performClick()
        compose.onNodeWithText("Iniciar sesión").assertIsDisplayed()
        compose.onNodeWithText("Iniciar sesión").performClick()
        compose.onNodeWithText("INICIAR SESIÓN").assertIsDisplayed()

        // Open menu and navigate to Register
        compose.onNodeWithContentDescription("Menu").performClick()
        compose.onNodeWithText("Registrarse").assertIsDisplayed()
        compose.onNodeWithText("Registrarse").performClick()
        compose.onNodeWithText("CREAR CUENTA").assertIsDisplayed()
    }

    @Test
    fun cartIconNavigatesToCartScreen() {
        compose.onNodeWithContentDescription("Carrito").assertIsDisplayed()
        compose.onNodeWithContentDescription("Carrito").performClick()
        compose.onNodeWithText("CARRITO").assertIsDisplayed()
        // Going back home via button on Cart
        compose.onNodeWithText("Volver al menú principal").assertIsDisplayed()
    }
}
