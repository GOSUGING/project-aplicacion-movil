package com.example.levelup.ui.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.levelup.MainActivity
import org.junit.Rule
import org.junit.Test

class HomeScreenFooterChipsVisibleTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun footer_chips_labels_visible_after_scroll() {

        // 1️⃣ Espera a que se cargue HomeScreen
        composeRule.waitUntil(timeoutMillis = 8000) {
            composeRule.onAllNodesWithText("Equipamiento. Poder. Estilo.").fetchSemanticsNodes().isNotEmpty()
        }

        // 2️⃣ Scroll dentro del LazyColumn
        composeRule
            .onNodeWithTag("home_list")
            .performScrollToNode(hasTestTag("footer_chips"))

        // 3️⃣ Footer existe
        composeRule
            .onNodeWithTag("footer_chips")
            .assertExists("Footer no encontrado en HomeScreen")
            .assertIsDisplayed()

        // 4️⃣ Verificar chips de pago
        val labels = listOf("PayPal", "Visa", "Mastercard", "Apple Pay", "Google Pay")

        labels.forEach { label ->
            composeRule
                .onNodeWithText(label, substring = false)
                .assertExists("Chip '$label' no existe")
                .assertIsDisplayed()
        }
    }
}
