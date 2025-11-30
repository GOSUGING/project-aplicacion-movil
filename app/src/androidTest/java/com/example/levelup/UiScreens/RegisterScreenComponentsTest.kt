package com.example.levelup.UiScreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.levelup.ui.screens.BirthDateField
import com.example.levelup.ui.screens.NeoField
import com.example.levelup.ui.screens.PasswordNeo
import org.junit.Rule
import org.junit.Test

class RegisterScreenComponentsTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun neoFieldShowsLabel() {
        compose.setContent {
            NeoField(label = "Nombre", value = "", onChange = {})
        }
        compose.onNodeWithText("Nombre").assertIsDisplayed()
    }

    @Test
    fun passwordNeoShowsLabel() {
        compose.setContent {
            PasswordNeo(label = "Contraseña", value = "", onChange = {})
        }
        compose.onNodeWithText("Contraseña").assertIsDisplayed()
    }

    @Test
    fun birthDateFieldShowsPlaceholderWhenBlank() {
        compose.setContent {
            BirthDateField(value = "", onSelect = {})
        }
        compose.onNodeWithText("Fecha de nacimiento (+18)").assertIsDisplayed()
    }
}

