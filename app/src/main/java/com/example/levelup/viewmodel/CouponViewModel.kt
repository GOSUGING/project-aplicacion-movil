package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.data.repository.CouponsRepository
import com.example.levelup.data.dto.CouponDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CouponUiState(
    val coupon: CouponDTO? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CouponsViewModel @Inject constructor(
    private val repo: CouponsRepository
) : ViewModel() {

    private val _ui = MutableStateFlow(CouponUiState())
    val ui = _ui.asStateFlow()

    fun fetchCoupon(code: String) {
        if (code.isBlank()) {
            _ui.update { it.copy(error = "Ingresa un cupón") }
            return
        }

        viewModelScope.launch {
            _ui.update { it.copy(isLoading = true, error = null) }

            val result = repo.findCoupon(code)

            result.fold(
                onSuccess = { coupon ->
                    _ui.update { it.copy(coupon = coupon, isLoading = false) }
                },
                onFailure = { e ->
                    _ui.update { it.copy(error = "Cupón inválido", isLoading = false) }
                }
            )
        }
    }
}
