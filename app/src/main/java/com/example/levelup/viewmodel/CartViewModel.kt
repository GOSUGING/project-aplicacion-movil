package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.data.dto.AddItemRequest
import com.example.levelup.data.repository.CartRepository
import com.example.levelup.data.dto.CartResponse
import com.example.levelup.data.dto.ProductDTO
import com.example.levelup.data.session.SessionManager
import com.example.levelup.viewmodel.state.CartUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class CartViewModel @Inject constructor(
    private val repo: CartRepository,
    val session: SessionManager
) : ViewModel() {

    private val _ui = MutableStateFlow(CartUiState())
    val ui = _ui.asStateFlow()

    // ======================================================
    // CARGAR CARRITO
    // ======================================================
    fun loadCart() {
        val user = session.getCurrentUser()

        if (user == null) {
            _ui.update { it.copy(error = "Usuario no autenticado", items = emptyList()) }
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
                    // Asegurar que la lista se vacía en caso de error de carga
                    _ui.update {
                        it.copy(
                            items = emptyList(),
                            error = e.message ?: "Error al cargar carrito",
                            isLoading = false
                        )
                    }
                }
            )
        }
    }

    // ======================================================
    // AGREGAR PRODUCTO
    // ======================================================
    fun addProduct(product: ProductDTO, qty: Int) {
        val user = session.getCurrentUser() ?: return

        viewModelScope.launch {

            val req = AddItemRequest(
                productId = product.id,
                qty = qty,
                name = product.name,
                price = product.price,
                imageUrl = product.img   // 👈 CORRECCIÓN AQUÍ
            )

            val result = repo.addToCart(user.id, req)

            result.fold(
                onSuccess = {
                    loadCart()
                },
                onFailure = { e ->
                    _ui.update { it.copy(error = e.message ?: "Error al agregar producto") }
                }
            )
        }
    }

    // ======================================================
    // ELIMINAR ITEM
    // ======================================================
    fun deleteItem(itemId: Long) {
        val user = session.getCurrentUser() ?: return

        viewModelScope.launch {
            val result = repo.deleteItem(user.id, itemId)

            result.fold(
                onSuccess = {
                    loadCart()
                },
                onFailure = { e ->
                    _ui.update { it.copy(error = e.message ?: "Error al eliminar item") }
                }
            )
        }
    }

    // ======================================================
    // 🛑 VACIAR CARRITO (USADO TRAS CHECKOUT EXITOSO)
    // ======================================================
    fun clearCart() {
        val user = session.getCurrentUser() ?: return

        viewModelScope.launch {
            _ui.update { it.copy(isLoading = true, error = null) }

            // 🛑 Llama a la función de limpieza del repositorio real
            val result = repo.clearAllItems(user.id)

            result.fold(
                onSuccess = {
                    // Si el backend confirmó el borrado, actualizamos el estado local a vacío
                    _ui.update {
                        it.copy(
                            items = emptyList(),
                            isLoading = false,
                            error = null
                        )
                    }
                },
                onFailure = { e ->
                    // Si el borrado falla (ej. error de red), mostramos el error
                    _ui.update {
                        it.copy(
                            error = e.message ?: "Error al vaciar carrito",
                            isLoading = false
                        )
                    }
                }
            )
        }
    }


    // ======================================================
    // CONTADOR DEL CARRITO (para el badge)
    // ======================================================
    val cartItemCount = ui
        .map { state -> state.items.sumOf { it.qty } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )
}