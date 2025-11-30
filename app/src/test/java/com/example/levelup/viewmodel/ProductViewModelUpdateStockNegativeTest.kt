package com.example.levelup.viewmodel

import com.example.levelup.data.dto.ProductDTO
import com.example.levelup.data.repository.ProductRepository
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ProductViewModelUpdateStockNegativeTest {
    @Test
    fun updateStock_negative_noCallsAndNoMessage() {
        val repo = mockk<ProductRepository>()
        val vm = ProductViewModel(repo)

        val p = ProductDTO(1L, "Mouse", "Desc", 300, "/img", "peripherals", 10)
        vm.updateStock(p, -5)

        coVerify(exactly = 0) { repo.updateProduct(any()) }
        assertNull(vm.ui.value.successMessage)
        assertNull(vm.ui.value.error)
    }
}

