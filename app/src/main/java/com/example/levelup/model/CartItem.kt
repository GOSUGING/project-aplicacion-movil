package com.example.levelup.model

// Un CartItem contiene un Producto y la cantidad de ese producto en el carrito.
// Usamos "var" para la cantidad, así podemos modificarla fácilmente (ej: al añadir más unidades).
data class CartItem(
    val product: Product,
    var quantity: Int
)
