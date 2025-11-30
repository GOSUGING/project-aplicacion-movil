package com.example.levelup.uiscreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.levelup.ui.screens.SummaryRow
import org.junit.Rule
import org.junit.Test

class PurchaseSummaryRowHighlightTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun summary_row_renders_label_and_value_highlight() {
        compose.setContent { SummaryRow(label = "Total Final", value = 999, highlight = true) }
        compose.onNodeWithText("Total Final").assertIsDisplayed()
        compose.onNodeWithText("$999").assertIsDisplayed()
    }
}

