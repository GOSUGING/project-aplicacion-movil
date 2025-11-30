package com.example.levelup.ModelTest

import com.example.levelup.data.model.Sale
import com.example.levelup.data.model.SaleItem
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.Date

class SaleModelTest {

    @Test
    fun totalUnitsSold_sumsQuantities() {
        val items = listOf(
            SaleItem(10, "Mouse", 2, 300.0),
            SaleItem(20, "Keyboard", 1, 500.0)
        )
        val sale = Sale(1, 7, "U", 1100.0, Date(), items)
        assertEquals(3, sale.totalUnitsSold())
    }

    @Test
    fun calculatedTotal_matchesSumOfItems() {
        val items = listOf(
            SaleItem(10, "Mouse", 2, 300.0),
            SaleItem(20, "Keyboard", 1, 500.0)
        )
        val sale = Sale(1, 7, "U", 0.0, Date(), items)
        assertEquals(1100.0, sale.calculatedTotal, 0.0)
    }

    @Test
    fun emptyItems_zeroTotals() {
        val sale = Sale(1, 7, "U", 0.0, Date(), emptyList())
        assertEquals(0, sale.totalUnitsSold())
        assertEquals(0.0, sale.calculatedTotal, 0.0)
    }
}
