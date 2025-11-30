package com.example.levelup.uiscreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.example.levelup.data.dto.ProductDTO
import com.example.levelup.data.network.ProductApi
import com.example.levelup.data.repository.ProductRepository
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
class AdminProductsScreenBackButtonClickableTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun big_back_button_is_clickable() = runTest(UnconfinedTestDispatcher()) {
        val api = mockk<ProductApi>()
        val repo = mockk<ProductRepository>()
        coEvery { api.getProducts() } returns listOf(ProductDTO(1L, "Teclado", "Mecánico", 500, "/img", "peripherals", 3))

        val vm = AdminViewModel(repo, api)

        compose.setContent {
            val nav = rememberNavController()
            com.example.levelup.ui.screens.AdminProductsScreen(navController = nav, vm = vm)
        }

        advanceUntilIdle()

        compose.onNodeWithText("Volver al Panel Admin").assertIsDisplayed().performClick()
    }
}

