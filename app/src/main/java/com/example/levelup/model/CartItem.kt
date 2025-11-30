// Un CartItem contiene un Producto y la cantidad de ese producto en el carrito.
// Usamos "var" para la cantidad, así podemos modificarla fácilmente (ej: al añadir más unidades).
package com.example.levelup.model

data class CartItem(
    val id: Long,
    val productId: Long,
    val name: String,
    val price: Int,
    val qty: Int,               // ✔ ESTE CAMPO DEBE LLAMARSE IGUAL QUE EL BACKEND
    val imageUrl: String
)
