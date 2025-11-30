package com.example.levelup.UiScreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
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
class AdminProductsScreenFabVisibleTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun fab_is_visible() = runTest(UnconfinedTestDispatcher()) {
        val repo = mockk<ProductRepository>(relaxed = true)
        val api = mockk<ProductApi>()
        coEvery { api.getProducts() } returns listOf(ProductDTO(1L, "Mouse", "Óptico", 300, "/img", "peripherals", 2))
        val vm = AdminViewModel(repo, api)
        advanceUntilIdle()

        compose.setContent {
            val nav = rememberNavController()
            com.example.levelup.ui.screens.AdminProductsScreen(navController = nav, vm = vm)
        }

        compose.onNodeWithContentDescription("Volver").assertIsDisplayed()
    }
}

