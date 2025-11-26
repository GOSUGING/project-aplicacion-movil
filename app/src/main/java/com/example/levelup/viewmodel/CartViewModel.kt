package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.data.CartRepository
import com.example.levelup.data.session.SessionManager
import com.example.levelup.model.CartItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CartUiState(
    val items: List<CartItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repo: CartRepository,
    private val session: SessionManager
) : ViewModel() {

    private val _ui = MutableStateFlow(CartUiState())
    val ui = _ui.asStateFlow()

    // ---------------------------------------------------------
    // Cargar carrito del usuario logueado
    // ---------------------------------------------------------
    fun loadCart() {
        val user = session.getCurrentUser()
        if (user == null) {
            _ui.update { it.copy(error = "Usuario no autenticado") }
            return
        }

        viewModelScope.launch {
            _ui.update { it.copy(isLoading = true, error = null) }

            val result = repo.getCart(user.id)

            result.fold(
                onSuccess = { items ->
                    _ui.update { it.copy(items = items, isLoading = false) }
                },
                onFailure = { e ->
                    _ui.update {
                        it.copy(
                            error = e.message ?: "Error al cargar carrito",
                            isLoading = false
                        )
                    }
                }
            )
        }
    }

    // ---------------------------------------------------------
    // Agregar producto al carrito
    // ---------------------------------------------------------
    fun addProduct(productId: Long, qty: Int) {
        val user = session.getCurrentUser() ?: return

        viewModelScope.launch {
            try {
                repo.addToCart(user.id, productId, qty)
                loadCart()
            } catch (e: Exception) {
                _ui.update { it.copy(error = "Error al agregar producto: ${e.message}") }
            }
        }
    }

    // ---------------------------------------------------------
    // Eliminar item del carrito
    // ---------------------------------------------------------
    fun deleteItem(itemId: Long) {
        viewModelScope.launch {
            try {
                repo.deleteItem(itemId)
                loadCart()
            } catch (e: Exception) {
                _ui.update { it.copy(error = "Error eliminando: ${e.message}") }
            }
        }
    }

    // ---------------------------------------------------------
    // Contador del carrito (top bar)
    // ---------------------------------------------------------
    val cartItemCount = ui
        .map { state -> state.items.sumOf { it.quantity } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )
}
