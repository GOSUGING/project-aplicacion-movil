package com.example.levelup.FlowTest

import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class FlowBasicsTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun updatesAreReflectedInUI() {
        val flow = MutableStateFlow("Hola")
        compose.setContent {
            val text by flow.collectAsState()
            Text(text)
        }
        compose.onNodeWithText("Hola").assertIsDisplayed()
        compose.runOnIdle { flow.value = "Mundo" }
        compose.onNodeWithText("Mundo").assertIsDisplayed()
    }
}

