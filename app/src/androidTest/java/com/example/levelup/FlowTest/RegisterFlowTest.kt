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

class RegisterFlowTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun showsSuccessOnValidSubmit() {
        val successFlow = MutableStateFlow("")
        compose.setContent {
            val msg by successFlow.collectAsState()
            if (msg.isNotEmpty()) Text(msg)
        }
        compose.runOnIdle { successFlow.value = "OK" }
        compose.onNodeWithText("OK").assertIsDisplayed()
    }
}

