package com.example.levelup.UiScreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.levelup.ui.screens.PurchaseResultScreen
import org.junit.Rule
import org.junit.Test

class PurchaseResultScreenButtonsTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun buttonsRenderProperly() {
        compose.setContent {
            PurchaseResultScreen(onBackHome = {}, onViewBills = {})
        }
        compose.onNodeWithText("Compra realizada con éxito").assertIsDisplayed()
        compose.onNodeWithText("Volver al inicio").assertIsDisplayed()
        compose.onNodeWithText("Ver mis compras").assertIsDisplayed()
    }
}

