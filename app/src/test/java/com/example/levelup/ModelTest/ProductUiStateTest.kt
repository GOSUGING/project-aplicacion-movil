package com.example.levelup.ModelTest

import com.example.levelup.data.dto.ProductDTO
import com.example.levelup.model.ProductUiState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ProductUiStateTest {
    @Test
    fun defaultsAreEmpty() {
        val st = ProductUiState()
        assertEquals(0, st.products.size)
        assertEquals(null, st.error)
        assertEquals(false, st.isLoading)
        assertEquals(null, st.successMessage)
    }

    @Test
    fun canHoldProducts() {
        val p = ProductDTO(1L, "Mouse", "D", 300, "/img", "peripherals", 10)
        val st = ProductUiState(products = listOf(p))
        assertEquals(1, st.products.size)
        assertEquals("Mouse", st.products[0].name)
    }
}
