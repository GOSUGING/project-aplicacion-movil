package com.example.levelup.data.dto

// DTO para el cuerpo de la petición POST (addItem)
data class AddItemRequest(
    val productId: Long,
    val qty: Int,            // 🔥 ESTE CAMPO ES EL QUE EL BACKEND SÍ USA
    val name: String,
    val price: Int,
    val imageUrl: String?
)
