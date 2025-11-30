package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.data.repository.ProductRepository
import com.example.levelup.data.dto.ProductDTO
import com.example.levelup.viewmodel.state.ProductUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repo: ProductRepository
) : ViewModel() {

    private val _ui = MutableStateFlow(ProductUiState())
    val ui = _ui.asStateFlow()

    // ============================
    // Lista de productos
    // ============================
    fun loadProducts() {
        viewModelScope.launch {
            _ui.update { it.copy(isLoading = true, error = null) }

            try {
                val list = repo.getProducts()
                _ui.update { it.copy(products = list, isLoading = false) }

            } catch (e: Exception) {
                _ui.update {
                    it.copy(
                        error = e.message ?: "Error desconocido",
                        isLoading = false
                    )
                }
            }
        }
    }

    // ============================
    // Detalle de producto
    // ============================
    fun loadProductById(id: Long) {
        viewModelScope.launch {
            _ui.update { it.copy(isLoading = true, error = null) }

            try {
                val product = repo.getProductById(id)
                _ui.update {
                    it.copy(
                        selectedProduct = product,
                        isLoading = false
                    )
                }

            } catch (e: Exception) {
                _ui.update {
                    it.copy(
                        error = "No se pudo cargar el producto",
                        isLoading = false
                    )
                }
            }
        }
    }

    // ============================
    // Actualizar stock
    // ============================
    fun updateStock(product: ProductDTO, newStock: Int) {
        if (newStock < 0) return

        viewModelScope.launch {
            try {
                val updated = product.copy(stock = newStock)
                repo.updateProduct(updated)
                loadProducts()

                _ui.update {
                    it.copy(successMessage = "Stock actualizado", error = null)
                }

            } catch (e: Exception) {
                _ui.update {
                    it.copy(error = "Error al actualizar stock: ${e.message}")
                }
            }
        }
    }
}
