package com.example.levelup.viewmodel

import com.example.levelup.data.dto.ProductDTO
import com.example.levelup.data.repository.ProductRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductViewModelUpdateStockSuccessFlowTest {
    private val dispatcher = UnconfinedTestDispatcher()

    @Test
    fun updateStock_success_setsMessage_and_reloads() = runTest(dispatcher) {
        val repo = mockk<ProductRepository>()
        val p = ProductDTO(1L, "Mouse", "D", 300, "/img", "peripherals", 10)
        val updated = p.copy(stock = 20)
        coEvery { repo.getProducts() } returns listOf(p)
        coEvery { repo.updateProduct(any()) } returns updated

        val vm = ProductViewModel(repo)
        vm.loadProducts()
        runCurrent()
        advanceUntilIdle()

        vm.updateStock(p, 20)
        runCurrent()
        advanceUntilIdle()

        assertNull(vm.ui.value.error)
        coVerify(exactly = 1) { repo.updateProduct(any()) }
        coVerify(atLeast = 1) { repo.getProducts() }
    }
}
