// En CartViewModel.kt
package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import com.example.levelup.model.CartItem
import com.example.levelup.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.StateFlow // Importa StateFlow
import kotlinx.coroutines.flow.map // Importa el operador map
import androidx.lifecycle.viewModelScope // Importa el scope del ViewModel
import kotlinx.coroutines.flow.SharingStarted // Importa SharingStarted
import kotlinx.coroutines.flow.stateIn // Importa stateIn

class CartViewModel : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems = _cartItems.asStateFlow()

    // --- ¡AÑADE ESTA PROPIEDAD! ---
    // Expone la cantidad de ítems como un StateFlow.
    // Se calcula automáticamente cada vez que `_cartItems` cambia.
    val cartItemCount: StateFlow<Int> = _cartItems
        .map { it.size } // Transforma la lista de ítems en su tamaño (un número)
        .stateIn(
            scope = viewModelScope, // El ciclo de vida del cálculo
            started = SharingStarted.WhileSubscribed(5000), // Empieza cuando hay un suscriptor
            initialValue = 0 // Valor inicial
        )
    // ------------------------------------

    fun addToCart(product: Product) {
        val currentItems = _cartItems.value.toMutableList()
        val existingItem = currentItems.find { it.product.id == product.id }

        if (existingItem != null) {
            existingItem.quantity++
        } else {
            currentItems.add(CartItem(product = product, quantity = 1))
        }
        _cartItems.value = currentItems
    }

    fun removeOne(productId: Int) {
        val currentItems = _cartItems.value.toMutableList()
        val existingItem = currentItems.find { it.product.id == productId }

        if (existingItem != null) {
            if (existingItem.quantity > 1) {
                existingItem.quantity--
            } else {
                currentItems.removeAll { it.product.id == productId }
            }
        }
        _cartItems.value = currentItems
    }

    fun removeFromCart(productId: Int) {
        _cartItems.value = _cartItems.value.filter { it.product.id != productId }
    }

    fun totalPrice(): Double {
        return _cartItems.value.sumOf { it.product.price * it.quantity } // <-- Corrección
    }
}
