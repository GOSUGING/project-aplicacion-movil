package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.data.network.ProductApi
import com.example.levelup.data.dto.ProductDTO // Asegúrate de que este DTO existe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// --- 1. ESTADO DE LA UI ---
data class AdminProductUiState(
    val products: List<ProductDTO> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class AdminViewModel @Inject constructor(
    // Solo se inyectan las dependencias necesarias para la gestión de productos
    private val productApi: ProductApi
) : ViewModel() {

    private val _ui = MutableStateFlow(AdminProductUiState())
    val ui = _ui.asStateFlow()

    init {
        // Cargar productos automáticamente al inicio
        loadProducts()
    }

    // --- 2. CARGA DE PRODUCTOS ---
    fun loadProducts() {
        if (_ui.value.isLoading) return
        _ui.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            try {
                // Llama al API para obtener la lista de productos
                val fetchedProducts = productApi.getProducts()
                _ui.update { it.copy(products = fetchedProducts, isLoading = false) }
            } catch (e: Exception) {
                _ui.update {
                    it.copy(
                        error = "Error al cargar el inventario: ${e.message}",
                        isLoading = false
                    )
                }
                e.printStackTrace()
            }
        }
    }

    // --- 3. ACTUALIZACIÓN DE STOCK ---
    fun updateStock(id: Long, amount: Int) {
        viewModelScope.launch {
            _ui.update { it.copy(isLoading = true, error = null) }
            try {
                // Llama al API para actualizar el stock del producto específico
                productApi.updateStock(id, amount)

                // Recarga la lista para reflejar el cambio en la UI
                loadProducts()
            } catch (e: Exception) {
                _ui.update {
                    it.copy(
                        error = "Error al actualizar stock: ${e.message}",
                        isLoading = false
                    )
                }
                e.printStackTrace()
            }
        }
    }
}