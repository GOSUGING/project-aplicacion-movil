package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.data.PaymentRepository
import com.example.levelup.data.dto.PaymentDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PaymentUiState(
    val payments: List<PaymentDTO> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val checkoutSuccess: Boolean = false
)

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val repo: PaymentRepository
) : ViewModel() {

    private val _ui = MutableStateFlow(PaymentUiState())
    val ui = _ui.asStateFlow()

    fun loadPayments() {
        viewModelScope.launch {
            _ui.update { it.copy(isLoading = true) }

            val result = repo.getPayments()

            result.fold(
                onSuccess = { list ->
                    _ui.update { it.copy(payments = list, isLoading = false) }
                },
                onFailure = { e ->
                    _ui.update { it.copy(error = e.message, isLoading = false) }
                }
            )
        }
    }

    fun checkout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _ui.update { it.copy(isLoading = true) }

            val result = repo.checkout()

            result.fold(
                onSuccess = {
                    _ui.update { it.copy(isLoading = false, checkoutSuccess = true) }
                    onSuccess()
                },
                onFailure = { e ->
                    _ui.update { it.copy(error = e.message, isLoading = false) }
                }
            )
        }
    }
}
