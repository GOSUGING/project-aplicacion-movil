package com.example.levelup.FlowTest

import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.levelup.viewmodel.state.CartUiState
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class CartFlowTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun subtotalUpdatesFromItems() {
        val flow = MutableStateFlow(CartUiState())
        compose.setContent {
            val ui by flow.collectAsState()
            Text("Subtotal: ${ui.subtotal}")
        }
        compose.onNodeWithText("Subtotal: 0").assertIsDisplayed()
        compose.runOnIdle {
            flow.value = CartUiState(
                items = listOf(
                    com.example.levelup.model.CartItem(1L, 10L, "Mouse", 300, 2, "/img"),
                    com.example.levelup.model.CartItem(2L, 20L, "Teclado", 500, 1, "/img2")
                )
            )
        }
        compose.onNodeWithText("Subtotal: 1100").assertIsDisplayed()
    }
}

