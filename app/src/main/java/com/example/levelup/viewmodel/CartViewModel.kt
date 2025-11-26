package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.data.dto.AddItemRequest
import com.example.levelup.data.repository.CartRepository
import com.example.levelup.data.dto.CartResponse
import com.example.levelup.data.session.SessionManager
import com.example.levelup.model.CartItem
import com.example.levelup.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// ==========================================================
//  ESTADO DEL CARRITO
// ==========================================================

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repo: CartRepository,
    private val session: SessionManager
) : ViewModel() {

    private val _ui = MutableStateFlow(CartUiState())
    val ui = _ui.asStateFlow()

    // ==========================================================
    // CARGAR CARRITO
    // ==========================================================
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
                onSuccess = { response: CartResponse ->
                    _ui.update {
                        it.copy(
                            items = response.items,
                            isLoading = false,
                            error = null
                        )
                    }
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

    // ==========================================================
    // AGREGAR PRODUCTO
    // ==========================================================
    fun addProduct(product: Product, qty: Int) {
        val user = session.getCurrentUser() ?: return

        viewModelScope.launch {

            val req = AddItemRequest(
                productId = product.id,
                qty = qty,             // ✔ nombre correcto que usa el backend
                name = product.name,
                price = product.price,
                imageUrl = product.imageUrl
            )

            val result = repo.addToCart(user.id, req)

            result.fold(
                onSuccess = {
                    loadCart()          // 🔥 AHORA RECARGA EL CARRITO
                },
                onFailure = { e ->
                    _ui.update { it.copy(error = e.message ?: "Error al agregar producto") }
                }
            )
        }
    }

    // ==========================================================
    // ELIMINAR ITEM
    // ==========================================================
    fun deleteItem(itemId: Long) {
        val user = session.getCurrentUser()

        if (user == null) {
            _ui.update { it.copy(error = "❌ Usuario no autenticado para eliminar producto") }
            return
        }

        viewModelScope.launch {
            val result = repo.deleteItem(user.id, itemId)

            result.fold(
                onSuccess = {
                    loadCart()
                },
                onFailure = { e ->
                    _ui.update { it.copy(error = e.message ?: "Error al eliminar producto") }
                }
            )
        }
    }

    // ==========================================================
    // CONTADOR DEL CARRITO
    // ==========================================================
    val cartItemCount = ui
        .map { state -> state.items.sumOf { it.qty } }  // 🔥 CAMBIADO quantity → qty
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )
}
