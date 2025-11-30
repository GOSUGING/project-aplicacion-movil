package com.example.levelup.uiscreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.example.levelup.data.repository.ProductRepository
import com.example.levelup.data.dto.ProductDTO
import com.example.levelup.data.network.ProductApi
import com.example.levelup.viewmodel.AdminViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AdminProductsScreenRenderAndStockButtonsTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun list_renders_and_stock_buttons_update() = runTest(UnconfinedTestDispatcher()) {
        val repo = mockk<ProductRepository>(relaxed = true)
        val api = mockk<ProductApi>()
        var currentStock = 3
        val base = ProductDTO(1L, "Teclado", "Mecánico", 500, "/img", "peripherals", currentStock)
        coEvery { api.getProducts() } answers { listOf(base.copy(stock = currentStock)) }
        coEvery { repo.updateProduct(any()) } answers {
            val updated = firstArg<ProductDTO>()
            currentStock = updated.stock
            updated
        }
        val vm = AdminViewModel(repo, api)
        advanceUntilIdle()

        compose.setContent {
            val nav = rememberNavController()
            com.example.levelup.ui.screens.AdminProductsScreen(navController = nav, vm = vm)
        }

        compose.onNodeWithText("GESTIÓN DE PRODUCTOS").assertIsDisplayed()
        compose.onNodeWithText("Teclado").assertIsDisplayed()
        compose.onNodeWithText("Precio: $500").assertIsDisplayed()
        compose.onNodeWithText("Stock: 3").assertIsDisplayed()

        compose.onNodeWithContentDescription("Sumar").assertIsDisplayed().performClick()
        advanceUntilIdle()
        compose.onNodeWithText("Stock: 4").assertIsDisplayed()
    }
}

