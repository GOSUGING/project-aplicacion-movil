package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.data.dto.CardPaymentDTO
import com.example.levelup.data.dto.CheckoutItem
import com.example.levelup.data.dto.CheckoutRequest
import com.example.levelup.data.dto.PaymentDTO
import com.example.levelup.data.dto.CheckoutResponseDTO
import com.example.levelup.data.repository.PaymentRepository
import com.example.levelup.model.CartItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PaymentUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val payments: List<PaymentDTO> = emptyList(),
    val successMessage: String? = null
)

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val repo: PaymentRepository
) : ViewModel() {

    private val _ui = MutableStateFlow(PaymentUiState())
    val ui = _ui.asStateFlow()

    // ============================================
    // CARGAR HISTORIAL
    // ============================================
    fun loadPayments() {
        if (_ui.value.loading) return

        viewModelScope.launch {
            _ui.update { it.copy(loading = true, error = null, successMessage = null) }

            try {
                val result: Result<List<PaymentDTO>> = repo.getPayments()

                result.fold(
                    onSuccess = { list ->
                        _ui.update { it.copy(loading = false, payments = list) }
                    },
                    onFailure = { throwable ->
                        _ui.update { it.copy(loading = false, error = throwable.message) }
                    }
                )

            } catch (e: Exception) {
                _ui.update { it.copy(loading = false, error = e.message ?: "Error desconocido") }
            }
        }
    }

    // ============================================
    // CHECKOUT CORRECTO
    // ============================================
    fun checkout(
        userId: Long,
        items: List<CartItem>,
        total: Int,
        nombreUsuario: String,
        direccionEnvio: String,
        cardPaymentDTO: CardPaymentDTO,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {

            _ui.update { it.copy(loading = true, error = null, successMessage = null) }

            try {

                // 1. Se mapea la lista de CartItem a CheckoutItem
                val checkoutItems = items.map {
                    CheckoutItem(
                        productId = it.productId,
                        cantidad = it.qty,
                        price = it.price
                    )
                }

                // 🛑 CORRECCIÓN CLAVE: Se llama al repositorio con los argumentos individuales.
                // Ya no pasamos el objeto 'body' CheckoutRequest, sino los datos crudos.
                val result: Result<CheckoutResponseDTO> = repo.checkout(
                    userId = userId, // El primer argumento esperado es Long (userId)
                    items = checkoutItems,
                    total = total,
                    nombreUsuario = nombreUsuario,
                    direccionEnvio = direccionEnvio,
                    cardPaymentDTO = cardPaymentDTO
                )

                result.fold(
                    onSuccess = { responseDTO ->
                        _ui.update {
                            it.copy(
                                loading = false,
                                successMessage = responseDTO.message
                            )
                        }
                        onSuccess()
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
                _ui.update {
                    it.copy(
                        loading = false,
                        error = e.message ?: "Error de conexión o desconocido"
                    )
                }
            }
        }
    }
}