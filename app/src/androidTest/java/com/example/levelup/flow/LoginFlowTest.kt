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

class LoginFlowTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun showsErrorWhenFieldsEmpty() {
        val errorFlow = MutableStateFlow("")
        compose.setContent {
            val err by errorFlow.collectAsState()
            if (err.isNotEmpty()) Text(err)
        }
        compose.runOnIdle { errorFlow.value = "Completa todos los campos" }
        compose.onNodeWithText("Completa todos los campos").assertIsDisplayed()
    }
}

