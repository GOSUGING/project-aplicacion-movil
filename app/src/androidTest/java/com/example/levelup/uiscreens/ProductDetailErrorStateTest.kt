package com.example.levelup.uiscreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.example.levelup.data.repository.ProductRepository
import com.example.levelup.data.repository.CartRepository
import com.example.levelup.data.session.SessionManager
import com.example.levelup.data.session.UserSession
import com.example.levelup.viewmodel.CartViewModel
import com.example.levelup.viewmodel.ProductViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductDetailErrorStateTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun product_detail_shows_error_on_failure() = runTest(UnconfinedTestDispatcher()) {
        val productRepo = mockk<ProductRepository>()
        val cartRepo = mockk<CartRepository>()
        val session = mockk<SessionManager>()
        every { session.getCurrentUser() } returns UserSession(1, "Alice", "alice@example.com", "USER")

        coEvery { productRepo.getProductById(any()) } throws RuntimeException("fallo")

        val vm = ProductViewModel(productRepo)
        val cartVM = CartViewModel(cartRepo, session)

        compose.setContent {
            val nav = rememberNavController()
            com.example.levelup.ui.screens.ProductDetailScreen(
                productId = 99L,
                navController = nav,
                vm = vm,
                cartVM = cartVM
            )
        }

        advanceUntilIdle()

        compose.onNodeWithText("Error al cargar el producto").assertIsDisplayed()
    }
}

