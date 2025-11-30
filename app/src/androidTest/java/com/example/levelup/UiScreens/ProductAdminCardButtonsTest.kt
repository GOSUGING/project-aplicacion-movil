package com.example.levelup.UiScreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.levelup.data.dto.ProductDTO
import com.example.levelup.ui.screens.ProductAdminCard
import org.junit.Rule
import org.junit.Test

class ProductAdminCardButtonsTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun renders_and_increase_decrease_edit_fire() {
        var inc = 0
        var dec = 0
        var edit = 0
        val product = ProductDTO(1L, "Teclado", "Mecánico", 500, "/img", "peripherals", 3)

        compose.setContent {
            ProductAdminCard(product = product, onIncrease = { inc++ }, onDecrease = { dec++ }, onEdit = { edit++ })
        }

        compose.onNodeWithText("Teclado").assertIsDisplayed()
        compose.onNodeWithText("Precio: $500").assertIsDisplayed()
        compose.onNodeWithText("Stock: 3").assertIsDisplayed()

        compose.onNodeWithContentDescription("Sumar").assertIsDisplayed().performClick()
        compose.onNodeWithContentDescription("Restar").assertIsDisplayed().performClick()
        compose.onNodeWithContentDescription("Editar").assertIsDisplayed().performClick()

        assert(inc == 1 && dec == 1 && edit == 1)
    }
}

