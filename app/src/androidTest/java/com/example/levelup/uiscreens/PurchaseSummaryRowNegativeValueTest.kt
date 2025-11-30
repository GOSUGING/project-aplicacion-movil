package com.example.levelup.uiscreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.levelup.ui.screens.SummaryRow
import org.junit.Rule
import org.junit.Test

class PurchaseSummaryRowNegativeValueTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun summary_row_formats_negative_values() {
        compose.setContent { SummaryRow(label = "Descuento (10%)", value = -50, highlight = false) }
        compose.onNodeWithText("$-50").assertIsDisplayed()
    }
}

