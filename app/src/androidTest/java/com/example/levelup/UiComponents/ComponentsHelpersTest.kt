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

class ComponentsHelpersTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun glowTextShowsText() {
        compose.setContent { GlowText("Texto", Color.Magenta) }
        compose.onNodeWithText("Texto").assertIsDisplayed()
    }

    @Test
    fun rowWithIconShowsText() {
        compose.setContent { RowWithIcon("Perfil", Icons.Default.Person, Color.Magenta) }
        compose.onNodeWithText("Perfil").assertIsDisplayed()
    }
}

