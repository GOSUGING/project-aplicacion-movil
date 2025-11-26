package com.example.levelup.viewmodel

import com.example.levelup.model.CartItem

data class CartUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val items: List<CartItem> = emptyList(),
    val subtotal: Double = 0.0
)
