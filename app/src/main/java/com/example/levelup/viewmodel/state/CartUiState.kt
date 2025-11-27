package com.example.levelup.viewmodel.state

import com.example.levelup.model.CartItem

data class CartUiState(
    val items: List<CartItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val subtotal: Int
        get() = items.sumOf { it.qty * it.price }
}
