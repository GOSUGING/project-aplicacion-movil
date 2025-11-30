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

class AdminUsersFlowTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun listSizeIsShown() {
        val users = MutableStateFlow(emptyList<String>())
        compose.setContent {
            val list by users.collectAsState()
            Text("Usuarios: ${list.size}")
        }
        compose.onNodeWithText("Usuarios: 0").assertIsDisplayed()
        compose.runOnIdle { users.value = listOf("Ana") }
        compose.onNodeWithText("Usuarios: 1").assertIsDisplayed()
    }
}

