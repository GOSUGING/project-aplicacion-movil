package com.example.levelup.uiscreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.levelup.ui.screens.NeoField
import org.junit.Rule
import org.junit.Test

class NeoFieldDisplaysValueTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun neo_field_displays_current_value() {
        compose.setContent { NeoField(label = "Nombre completo", value = "Juan Pérez", onChange = {}) }
        compose.onNodeWithText("Juan Pérez").assertIsDisplayed()
    }
}

