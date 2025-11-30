package com.example.levelup.viewmodel

import com.example.levelup.data.dto.ProductDTO
import com.example.levelup.data.repository.ProductRepository
import io.kotest.core.spec.style.AnnotationSpec
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class ProductViewModelTest : AnnotationSpec() {

    private lateinit var repo: ProductRepository
    private lateinit var viewModel: ProductViewModel

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        repo = mockk()
        viewModel = ProductViewModel(repo)
    }

    @AfterEach
    fun cleanup() {
        Dispatchers.resetMain()
    }

    private fun fakeProduct(
        id: Long = 1,
        name: String = "Producto",
        price: Int = 9990,
        category: String = "consolas",
        stock: Int = 5
    ) = ProductDTO(
        id = id,
        name = name,
        description = "desc",
        price = price,
        img = "url",
        category = category,
        stock = stock
    )

    @Test
    fun `loadProducts actualiza ui con productos`() = runTest {
        val fakeList = listOf(
            fakeProduct(1, "PS5", 480000, "consolas", 3),
            fakeProduct(2, "Mario Kart", 45000, "juegos", 8)
        )

        coEvery { repo.getProducts() } returns fakeList

        viewModel.loadProducts()
        advanceUntilIdle()

        assertEquals(2, viewModel.ui.value.products.size)
        assertNull(viewModel.ui.value.error)
    }

    @Test
    fun `loadProducts muestra error cuando la API falla`() = runTest {
        coEvery { repo.getProducts() } throws Exception("API Error")

        viewModel.loadProducts()
        advanceUntilIdle()

        assertNotNull(viewModel.ui.value.error)
    }
}
