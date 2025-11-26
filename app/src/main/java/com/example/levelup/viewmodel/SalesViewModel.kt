package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.data.network.PaymentApi // O tu SalesApi real
import com.example.levelup.data.dto.PaymentDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// --- 1. ESTADO DE LA UI ---
data class SalesUiState(
    // Utilizamos PaymentDTO para la lista de ventas
    val sales: List<PaymentDTO> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SalesViewModel @Inject constructor(
    // Inyectamos la API necesaria para obtener los datos de pago/ventas
    private val paymentApi: PaymentApi
) : ViewModel() {

    // Se asume que 'ui' contendrá el estado de la lista de ventas
    private val _ui = MutableStateFlow(SalesUiState())
    val ui = _ui.asStateFlow()

    init {
        // Cargar las ventas automáticamente al inicio
        loadSales()
    }

    /**
     * Carga la lista de pagos/ventas desde el API.
     */
    fun loadSales() {
        if (_ui.value.isLoading) return
        _ui.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            try {
                // Llama al método de la API para obtener la lista de pagos
                val fetchedPayments = paymentApi.getPayments()

                // Actualiza el estado con la lista de pagos/ventas
                _ui.update { it.copy(sales = fetchedPayments, isLoading = false) }
            } catch (e: Exception) {
                // Manejo de errores
                _ui.update {
                    it.copy(
                        error = "Error al cargar las ventas: ${e.message}",
                        isLoading = false
                    )
                }
                e.printStackTrace()
            }
        }
    }
}