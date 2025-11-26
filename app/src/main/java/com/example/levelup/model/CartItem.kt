// Un CartItem contiene un Producto y la cantidad de ese producto en el carrito.
// Usamos "var" para la cantidad, así podemos modificarla fácilmente (ej: al añadir más unidades).
package com.example.levelup.model

data class CartItem(
    val id: Long,
    val userId: Long,
    val productId: Long,
    val quantity: Int,
    val name: String,
    val price: Int,
    val img: String
)
