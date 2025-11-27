package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.data.dto.PaymentDTO
import com.example.levelup.data.repository.PaymentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PaymentsHistoryUiState(
    val payments: List<PaymentDTO> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class PaymentsHistoryViewModel @Inject constructor(
    private val repo: PaymentRepository
) : ViewModel() {

    private val _ui = MutableStateFlow(PaymentsHistoryUiState())
    val ui = _ui.asStateFlow()

    fun loadPayments() {
        // Opcional: Evitar múltiples llamadas si ya se está cargando
        if (_ui.value.loading) return

        viewModelScope.launch {
            // Reiniciar estado de carga y errores previos
            _ui.update { it.copy(loading = true, error = null) }

            try {
                // El repositorio devuelve Result<List<PaymentDTO>>
                val result: Result<List<PaymentDTO>> = repo.getPayments()

                // 🛑 CORRECCIÓN: Usamos .fold() para manejar el éxito o el fallo
                result.fold(
                    onSuccess = { paymentsList ->
                        _ui.update {
                            it.copy(
                                loading = false,
                                payments = paymentsList // Asignamos la lista de pagos limpia
                            )
                        }
                    },
                    onFailure = { throwable ->
                        _ui.update {
                            it.copy(
                                loading = false,
                                error = throwable.message
                            )
                        }
                    }
                )

            } catch (e: Exception) {
                // Captura de excepciones de corrutinas o problemas inesperados
                _ui.update { it.copy(loading = false, error = e.message ?: "Error de conexión o desconocido") }
            }
        }
    }
}