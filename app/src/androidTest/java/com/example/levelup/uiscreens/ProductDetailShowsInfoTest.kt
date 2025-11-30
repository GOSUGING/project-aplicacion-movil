package com.example.levelup.uiscreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.example.levelup.data.dto.ProductDTO
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
class ProductDetailShowsInfoTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun product_detail_renders_basic_info() = runTest(UnconfinedTestDispatcher()) {
        val productRepo = mockk<ProductRepository>()
        val cartRepo = mockk<CartRepository>()
        val session = mockk<SessionManager>()
        every { session.getCurrentUser() } returns UserSession(1, "Alice", "alice@example.com", "USER")

        val product = ProductDTO(7L, "Auriculares", "Over-ear", 300, "/img", "peripherals", 5)
        coEvery { productRepo.getProductById(7L) } returns product

        val vm = ProductViewModel(productRepo)
        val cartVM = CartViewModel(cartRepo, session)

        compose.setContent {
            val nav = rememberNavController()
            com.example.levelup.ui.screens.ProductDetailScreen(
                productId = 7L,
                navController = nav,
                vm = vm,
                cartVM = cartVM
            )
        }

        advanceUntilIdle()

        compose.onNodeWithText("Auriculares").assertIsDisplayed()
        compose.onNodeWithText("$300").assertIsDisplayed()
        compose.onNodeWithText("Stock: 5").assertIsDisplayed()
        compose.onNodeWithText("⬅ VOLVER A PRODUCTOS").assertIsDisplayed()
    }
}

