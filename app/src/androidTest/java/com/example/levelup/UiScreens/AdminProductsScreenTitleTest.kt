package com.example.levelup.UiScreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.example.levelup.data.ProductRepository
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
class AdminProductsScreenTitleTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun titleIsShown() = runTest(UnconfinedTestDispatcher()) {
        val repo = mockk<ProductRepository>()
        val api = mockk<ProductApi>()
        coEvery { api.getProducts() } returns emptyList()
        val vm = AdminViewModel(repo, api)
        advanceUntilIdle()

        compose.setContent {
            val nav = rememberNavController()
            com.example.levelup.ui.screens.AdminProductsScreen(navController = nav, vm = vm)
        }

        compose.onNodeWithText("GESTIÓN DE PRODUCTOS").assertIsDisplayed()
    }
}

