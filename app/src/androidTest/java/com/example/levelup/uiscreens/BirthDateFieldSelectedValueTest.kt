package com.example.levelup.uiscreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.levelup.ui.screens.BirthDateField
import org.junit.Rule
import org.junit.Test

class BirthDateFieldSelectedValueTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun birth_date_field_shows_selected_value() {
        compose.setContent { BirthDateField(value = "2000-05-20", onSelect = {}) }
        compose.onNodeWithText("2000-05-20").assertIsDisplayed()
    }
}

