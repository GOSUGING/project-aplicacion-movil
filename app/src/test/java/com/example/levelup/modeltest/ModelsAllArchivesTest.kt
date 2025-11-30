package com.example.levelup.modeltest

import com.example.levelup.model.CartItem
import com.example.levelup.model.Product
import com.example.levelup.data.model.Sale
import com.example.levelup.data.model.SaleItem
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.Date

class ModelsAllArchivesTest {
    @Test
    fun productHoldsAllFields() {
        val pr = Product(1L, "Mouse", "Optico", 300, "/img", null, "peripherals", 10)
        assertEquals(1L, pr.id)
        assertEquals("peripherals", pr.category)
    }

    @Test
    fun cartItemProperties() {
        val ci = CartItem(1L, 10L, "Mouse", 300, 2, "/img")
        assertEquals(2, ci.qty)
        assertEquals(300, ci.price)
    }

    @Test
    fun saleTotalsComputed() {
        val items = listOf(
            SaleItem(10, "Mouse", 2, 300.0),
            SaleItem(20, "Keyboard", 1, 500.0)
        )
        val sale = Sale(1, 7, "U", 1100.0, Date(), items)
        assertEquals(3, sale.totalUnitsSold())
        assertEquals(1100.0, sale.calculatedTotal, 0.0)
    }
}
