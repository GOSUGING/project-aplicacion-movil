package com.example.levelup.UiScreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.levelup.ui.screens.PurchaseResultScreen
import org.junit.Rule
import org.junit.Test

class PurchaseResultScreenButtonsClickTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun click_buttons_fire_callbacks() {
        var backHome = 0
        var viewBills = 0

        compose.setContent {
            PurchaseResultScreen(onBackHome = { backHome++ }, onViewBills = { viewBills++ })
        }

        compose.onNodeWithText("Compra realizada con éxito").assertIsDisplayed()
        compose.onNodeWithText("Volver al inicio").assertIsDisplayed().performClick()
        compose.onNodeWithText("Ver mis compras").assertIsDisplayed().performClick()

        assert(backHome == 1 && viewBills == 1)
    }
}

