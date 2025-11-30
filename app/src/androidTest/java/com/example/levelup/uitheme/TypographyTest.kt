package com.example.levelup.UiTheme

import androidx.activity.ComponentActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.levelup.ui.theme.AppTypography
import org.junit.Rule
import org.junit.Test

class TypographyTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun appTypography_styles_canRenderText() {
        compose.setContent { ThemedSample() }
        compose.onNodeWithText("Titulo").assertIsDisplayed()
        compose.onNodeWithText("Descripcion").assertIsDisplayed()
        compose.onNodeWithText("Boton").assertIsDisplayed()
    }

    @Composable
    private fun ThemedSample() {
        MaterialTheme(typography = AppTypography) {
            Text("Titulo", style = MaterialTheme.typography.headlineLarge)
            Text("Descripcion", style = MaterialTheme.typography.bodyMedium)
            Text("Boton", style = MaterialTheme.typography.labelLarge)
        }
    }
}

