package com.example.levelup.data.dto

import com.example.levelup.model.CartItem

// DTO para la respuesta del servidor
data class CartResponse(
    val userId: Long,
    val items: List<CartItem>,
    val totalItems: Int,
    val subtotal: Double
)
