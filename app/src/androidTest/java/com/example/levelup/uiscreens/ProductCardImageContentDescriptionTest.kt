package com.example.levelup.uiscreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import com.example.levelup.data.dto.ProductDTO
import com.example.levelup.ui.screens.ProductCard
import org.junit.Rule
import org.junit.Test

class ProductCardImageContentDescriptionTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun product_card_image_has_content_description() {
        val product = ProductDTO(99L, "Micrófono", "USB", 60, "/img", "accesorios", 5)

        compose.setContent {
            ProductCard(product = product, onAddToCart = {}, onClick = {})
        }

        compose.onNodeWithContentDescription("Micrófono").assertIsDisplayed()
    }
}

