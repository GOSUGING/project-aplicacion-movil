package com.example.levelup.uiscreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import com.example.levelup.data.dto.ProductDTO
import com.example.levelup.ui.screens.ProductAdminCard
import org.junit.Rule
import org.junit.Test

class ProductAdminCardImageContentDescriptionTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun image_has_product_name_as_content_description() {
        val product = ProductDTO(30L, "Silla Gamer", "Ergonómica", 250, "/img", "accesorios", 4)

        compose.setContent {
            ProductAdminCard(product = product, onIncrease = {}, onDecrease = {}, onEdit = {})
        }

        compose.onNodeWithContentDescription("Silla Gamer").assertIsDisplayed()
    }
}

