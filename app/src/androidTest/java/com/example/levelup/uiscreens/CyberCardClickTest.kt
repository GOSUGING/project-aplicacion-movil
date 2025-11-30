package com.example.levelup.uiscreens

import androidx.activity.ComponentActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.levelup.ui.screens.CyberCard
import org.junit.Rule
import org.junit.Test

class CyberCardClickTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun cyber_card_click_triggers_callback() {
        var clicks = 0
        compose.setContent {
            CyberCard(label = "Gestión de Usuarios", color = androidx.compose.ui.graphics.Color.Cyan, icon = Icons.Default.Person) {
                clicks++
            }
        }

        compose.onNodeWithText("Gestión de Usuarios").assertIsDisplayed().performClick()
        assert(clicks == 1)
    }
}

