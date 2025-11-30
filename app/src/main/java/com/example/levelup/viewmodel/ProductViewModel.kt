package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.data.ProductRepository
import com.example.levelup.data.dto.ProductDTO

import com.example.levelup.model.ProductUiState
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

    fun loadProducts() {
        viewModelScope.launch {
            _ui.update { it.copy(isLoading = true, error = null) }

            try {
                val list = repo.getProducts()
                _ui.update {
                    it.copy(products = list, isLoading = false)
                }
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

    fun updateStock(product: ProductDTO, newStock: Int) {
        if (newStock < 0) return

        viewModelScope.launch {
            try {
                val updated = product.copy(stock = newStock)

                repo.updateProduct(updated)

                loadProducts()

                _ui.update {
                    it.copy(
                        successMessage = "Stock de '${product.name}' actualizado",
                        error = null
                    )
                }

            } catch (e: Exception) {
                _ui.update {
                    it.copy(error = "Error al actualizar stock: ${e.message}")
                }
            }
        }
    }
}
