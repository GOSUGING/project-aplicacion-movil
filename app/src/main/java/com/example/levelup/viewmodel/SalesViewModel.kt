package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.data.network.PaymentApi
import com.example.levelup.data.dto.PaymentDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SalesUiState(
    val sales: List<PaymentDTO> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SalesViewModel @Inject constructor(
    private val paymentApi: PaymentApi
) : ViewModel() {

    private val _ui = MutableStateFlow(SalesUiState())
    val ui = _ui.asStateFlow()

    init {
        loadSales()
    }

    fun loadSales() {
        if (_ui.value.isLoading) return

        _ui.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            try {
                // ✔ MÉTODO CORRECTO
                val fetchedPayments = paymentApi.getAllPayments()

                _ui.update { it.copy(sales = fetchedPayments, isLoading = false) }

            } catch (e: Exception) {
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
