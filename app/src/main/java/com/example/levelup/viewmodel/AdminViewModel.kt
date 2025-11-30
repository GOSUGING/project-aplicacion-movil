package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.data.dto.ProductDTO
import com.example.levelup.data.network.ProductApi
import com.example.levelup.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// ------------------------------------------------------------
// UI STATE
// ------------------------------------------------------------
data class AdminProductUiState(
    val products: List<ProductDTO> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val role: String = ""
)

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val repo: ProductRepository,
    private val productApi: ProductApi
) : ViewModel() {

    private val _ui = MutableStateFlow(AdminProductUiState())
    val ui = _ui.asStateFlow()

    init {
        loadProducts()
    }

    // ------------------------------------------------------------
    // SETEAR ROL DEL USUARIO
    // ------------------------------------------------------------
    fun setRole(role: String?) {
        _ui.update { it.copy(role = role?.uppercase() ?: "") }
    }

    // ------------------------------------------------------------
    // CARGAR PRODUCTOS
    // ------------------------------------------------------------
    fun loadProducts() {
        if (_ui.value.isLoading) return

        _ui.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            try {
                val list = productApi.getProducts()
                _ui.update {
                    it.copy(
                        products = list,
                        isLoading = false,
                        successMessage = "Inventario cargado correctamente"
                    )
                }
            } catch (e: Exception) {
                _ui.update {
                    it.copy(
                        error = "Error al cargar productos: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    // ------------------------------------------------------------
    // ACTUALIZAR STOCK USANDO updateProduct()
    // ------------------------------------------------------------
    fun editStock(product: ProductDTO, newStock: Int) {
        if (newStock < 0) return

        viewModelScope.launch {
            try {
                // Crear nuevo producto con stock actualizado
                val updatedProduct = product.copy(stock = newStock)

                // Llamar al backend
                repo.updateProduct(updatedProduct)

                // Recargar lista después del update
                loadProducts()

                // Refrescar UI sin perder mensaje
                _ui.update {
                    it.copy(
                        successMessage = "Stock de '${product.name}' actualizado correctamente",
                        error = null
                    )
                }

            } catch (e: Exception) {
                _ui.update {
                    it.copy(
                        error = "Error al actualizar stock: ${e.message}"
                    )
                }
            }
        }
    }




    // ------------------------------------------------------------
    // LIMPIAR MENSAJES
    // ------------------------------------------------------------
    fun clearMessages() {
        _ui.update { it.copy(error = null, successMessage = null) }
    }
}
