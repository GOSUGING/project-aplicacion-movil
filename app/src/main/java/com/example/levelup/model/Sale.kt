package com.example.levelup.data.model

import java.util.Date

data class Sale(
    val id: Int,
    val userId: Int,
    val userName: String,
    val total: Double, // Este campo ya almacena el total calculado
    val date: Date,
    val items: List<SaleItem>
) {
    /**
     * Devuelve el número total de unidades vendidas en esta transacción.
     * (Suma la propiedad 'quantity' de todos los SaleItems).
     */
    fun totalUnitsSold(): Int {
        return items.sumOf { it.quantity }
    }

    /**
     * Devuelve una propiedad calculada (read-only) que calcula el total real
     * sumando todos los items. Esto es útil para verificar que el campo 'total'
     * almacenado es correcto.
     */
    val calculatedTotal: Double
        get() = items.sumOf { it.quantity * it.price }
}

data class SaleItem(
    val productId: Int,
    val productName: String,
    val quantity: Int,
    val price: Double
)