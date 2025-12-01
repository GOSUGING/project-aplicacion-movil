package com.example.levelup.uiscreens

import androidx.activity.ComponentActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.levelup.ui.screens.CyberCard
import org.junit.Rule
import org.junit.Test

class CyberCardClickVentasTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun cyber_card_ventas_click_triggers_callback() {
        var clicks = 0
        compose.setContent {
            CyberCard(label = "Historial de Ventas", color = Color.Magenta, icon = Icons.Filled.ReceiptLong) {
                clicks++
            }
        }

        compose.onNodeWithText("Historial de Ventas").assertIsDisplayed().performClick()
        assert(clicks == 1)
    }
}

