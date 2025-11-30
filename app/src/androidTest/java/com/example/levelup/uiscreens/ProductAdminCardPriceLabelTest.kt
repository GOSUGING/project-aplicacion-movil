package com.example.levelup.uiscreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.levelup.data.dto.ProductDTO
import com.example.levelup.ui.screens.ProductAdminCard
import org.junit.Rule
import org.junit.Test

class ProductAdminCardPriceLabelTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun shows_price_label_correctly() {
        val product = ProductDTO(12L, "GPU", "RTX", 1999, "/img", "graficas", 2)

        compose.setContent {
            ProductAdminCard(product = product, onIncrease = {}, onDecrease = {}, onEdit = {})
        }

        compose.onNodeWithText("Precio: $1999").assertIsDisplayed()
    }
}

