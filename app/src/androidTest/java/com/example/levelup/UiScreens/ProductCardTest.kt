package com.example.levelup.UiScreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.levelup.data.dto.ProductDTO
import com.example.levelup.ui.screens.ProductCard
import org.junit.Rule
import org.junit.Test

class ProductCardTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun productCard_showsInfo_and_callbacksFire() {
        var clicks = 0
        val product = ProductDTO(1L, "Mouse", "Óptico", 300, "/img", "peripherals", 10)

        compose.setContent {
            ProductCard(
                product = product,
                onAddToCart = { clicks++ },
                onClick = { clicks++ }
            )
        }

        compose.onNodeWithText("Mouse").assertIsDisplayed()
        compose.onNodeWithText("$300").assertIsDisplayed()
        compose.onNodeWithText("Agregar al carrito").assertIsDisplayed().performClick()
        compose.onNodeWithText("Mouse").performClick()

        assert(clicks == 2)
    }
}

