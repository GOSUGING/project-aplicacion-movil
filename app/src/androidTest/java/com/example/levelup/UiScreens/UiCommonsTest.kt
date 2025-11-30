package com.example.levelup.UiScreens

import androidx.activity.ComponentActivity
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.levelup.ui.screens.appTextFieldColors
import org.junit.Rule
import org.junit.Test

class UiCommonsTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun textFieldColorsRenderLabel() {
        compose.setContent {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Nombre") },
                colors = appTextFieldColors()
            )
        }
        compose.onNodeWithText("Nombre").assertIsDisplayed()
    }
}

