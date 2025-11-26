package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.data.ProductRepository
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
            _ui.update { it.copy(isLoading = true) }

            val result = repo.getProducts()

            result.fold(
                onSuccess = { list ->
                    _ui.update { it.copy(products = list, isLoading = false) }
                },
                onFailure = { e ->
                    _ui.update { it.copy(error = e.message, isLoading = false) }
                }
            )
        }
    }

    // 🔥 MÉTODO CORRECTO PARA SUMAR STOCK
    fun updateStock(id: Long, amount: Int) {
        viewModelScope.launch {
            try {
                repo.updateStock(id, amount)
                loadProducts()   // vuelve a cargar productos actualizado
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
