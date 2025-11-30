package com.example.levelup.UiComponents

import androidx.activity.ComponentActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.levelup.ui.components.GlowText
import com.example.levelup.ui.components.RowWithIcon
import org.junit.Rule
import org.junit.Test

class ComponentsGlowRowTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun glowText_and_rowWithIcon_renderLabels() {
        compose.setContent {
            RowWithIcon(text = "Iniciar sesión", icon = Icons.Default.Person, tint = Color.Magenta)
            GlowText(text = "Productos", color = Color.Cyan)
        }

        compose.onNodeWithText("Iniciar sesión").assertIsDisplayed()
        compose.onNodeWithText("Productos").assertIsDisplayed()
    }
}

