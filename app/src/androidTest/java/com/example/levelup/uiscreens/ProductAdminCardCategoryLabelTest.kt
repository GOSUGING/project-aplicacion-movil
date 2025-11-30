package com.example.levelup.uiscreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.levelup.data.dto.ProductDTO
import com.example.levelup.ui.screens.ProductAdminCard
import org.junit.Rule
import org.junit.Test

class ProductAdminCardCategoryLabelTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun shows_category_label() {
        val product = ProductDTO(10L, "Mouse", "Óptico", 300, "/img", "peripherals", 8)

        compose.setContent {
            ProductAdminCard(product = product, onIncrease = {}, onDecrease = {}, onEdit = {})
        }

        compose.onNodeWithText("Categoría: peripherals").assertIsDisplayed()
    }
}

